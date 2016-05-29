package optimizer.gui.genetic_algorithm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.Solution;
import optimizer.domain.Element;
import optimizer.domain.Task;
import optimizer.gui.GraphPanel;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Chromosome;
import optimizer.solver.genetic_algorithm.Config;
import optimizer.solver.genetic_algorithm.Population;

public class GeneticAlgorithmDialog {
	private JDialog dialog;
	private Problem problem;
	private Config config;
	private GraphPanel gp;
	private GeneticAlgoThread algoThread;
	private JLabel generationLabel;
	private JLabel fitnessLabel;
	private JLabel totalTimeLabel;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	private DefaultCategoryDataset dataset;
	private volatile int selectedTask;
	private DefaultListModel<String> elementListModel;
	public GeneticAlgorithmDialog(JFrame frame, Problem problem, Config config) {
		this.dialog = new JDialog(frame, "Genetic Algorithm");
		this.problem = problem;
		this.config = config;
		this.gp = new GraphPanel(problem);
		dialog.setMaximumSize(new Dimension());
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (algoThread != null)
					algoThread.terminate();
			}
		});
		dialog.getContentPane().setLayout(new GridBagLayout());
		generationLabel = new JLabel("0");
		fitnessLabel = new JLabel("");
		totalTimeLabel = new JLabel("");
		dataset = new DefaultCategoryDataset();
		chart = ChartFactory.createLineChart(
				"Population fitness & Total project time",
				"generation",
				"value",
				dataset,
				PlotOrientation.VERTICAL,
				true, true, false);
		chartPanel = new ChartPanel(chart);

		JPanel bestInfo = new JPanel();
		bestInfo.setLayout(new FlowLayout());
		
		bestInfo.add(new JLabel("Generation:"));
		bestInfo.add(generationLabel);

		bestInfo.add(new JLabel("Fitness:"));
		bestInfo.add(fitnessLabel);

		bestInfo.add(new JLabel("Total time:"));
		bestInfo.add(totalTimeLabel);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(6, 6, 6, 6);
		c.weightx = 0.5;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		dialog.add(bestInfo, c);
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		dialog.add(chartPanel, c);
		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 0;
		dialog.add(createElementsPanel(), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 3;
		dialog.add(gp.getView(), c);
		dialog.setModal(false);
		gp.getView().setMinimumSize(new Dimension(800, 600));
		gp.getView().setPreferredSize(new Dimension(800, 600));
	}
	
	private Container createElementsPanel() {
		String[] taskNames = new String[problem.getTasks().size()];
		for (int i = 0; i < problem.getTasks().size(); i++) {
			taskNames[i] = problem.getTasks().get(i).getName();
		}
		JComboBox<String> elementComboBox = new JComboBox<String>(taskNames);
		elementComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedTask = elementComboBox.getSelectedIndex();
			}
		});
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
		JLabel taskLabel = new JLabel("Task: ");
		elementListModel = new DefaultListModel<String>();
		JList<String> elementList = new JList<String>(elementListModel);
		top.add(taskLabel);
		top.add(elementComboBox);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(elementList);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(top, BorderLayout.NORTH);
		panel.add(scrollPane);
		return panel;
	}

	public void show() {
		startGeneticAlgorithm(problem, config);
	}

	private void startGeneticAlgorithm(Problem problem, Config config) {
		Algorithm algorithm = new Algorithm(problem, config);
		final Population population = Optimizer.createStartingPopulation(algorithm);
		gp.update(population.getFittest());
		dialog.pack();
		dialog.setVisible(true);
		algoThread = new GeneticAlgoThread(algorithm, population);
		algoThread.start();
	}

	public class GeneticAlgoThread extends Thread {
		private volatile boolean running = false;
		private Population population;
		private Algorithm algorithm;
		public GeneticAlgoThread(Algorithm algorithm, Population population) {
			this.algorithm = algorithm;
			this.population = population;
		}
		@Override
		public void run() {
			running = true;
			Population p = population;
			int oldFitness = p.getFittest().getFitness();
			while (running) {
				p.showInfo();
				Solution s = p.getFittest();
				/*for (Task t : problem.getTasks()) {
					System.out.println("Task \"" + t.getName() + "\" [" + s.getTaskStartTime(t) + ", " + s.getTaskCompletionTime(t) + "]: ");
					Set<Element> elements = p.getFittest().getTaskAssignedElements(t);
					if (elements == null) continue;
					for (Element e : elements) {
						System.out.print(e.getName() + ", ");
					}
					System.out.println();
				}*/
				chart.setNotify(false);
				if (p.getFittest().getFitness() != oldFitness)
				{
					chart.setNotify(true);
					oldFitness = p.getFittest().getFitness();
				}
				dataset.addValue((float)p.getFittest().getFitness() / Chromosome.TOTAL_TIME_SCORE_MULTIPLIER, "fitness/" + Chromosome.TOTAL_TIME_SCORE_MULTIPLIER, "" + p.num());
				dataset.addValue(p.getFittest().getTotalTime(), "total time", "" + p.num());
				
				elementListModel.clear();
				for (Element e : p.getFittest().getTaskAssignedElements(problem.getTasks().get(selectedTask)))
					elementListModel.addElement(e.getName());
				
				p = algorithm.evolve(p);
				gp.update(p.getFittest());
				generationLabel.setText("" + p.num());
				fitnessLabel.setText("" + p.getFittest().getFitness());
				totalTimeLabel.setText("" + p.getFittest().getTotalTime());
				dialog.repaint();
			}
			p.showInfo();
		}

		public void terminate() {
			running = false;
		}
	}
}
