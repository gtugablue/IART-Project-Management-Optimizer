package optimizer.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class GeneticAlgorithmDialog {
	private JDialog dialog;
	private Problem problem;
	private GraphPanel gp;
	private GeneticAlgoThread algoThread;
	private JLabel fitnessLabel;
	private ChartPanel chartPanel;
	private DefaultCategoryDataset dataset;
	public GeneticAlgorithmDialog(JFrame frame, Problem problem) {
		this.dialog = new JDialog(frame, "Genetic Algorithm");
		this.problem = problem;
		this.gp = new GraphPanel(problem);
		dialog.setPreferredSize(new Dimension(800, 600));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (algoThread != null)
					algoThread.terminate();
			}
		});
		dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
		fitnessLabel = new JLabel("");
		dataset = new DefaultCategoryDataset();
		JFreeChart chart = ChartFactory.createLineChart(
				"Population fitness",
				"population",
				"fitness",
				dataset,
				PlotOrientation.VERTICAL,
				true, true, false);
		chartPanel = new ChartPanel(chart);
		
		JPanel header = new JPanel();
		header.setLayout(new FlowLayout());
		header.add(new JLabel("Fitness: "));
		header.add(fitnessLabel);
		header.add(chartPanel);
		
		dialog.add(header);
		dialog.add(gp.getView());
		dialog.setModal(false);
		gp.getView().setPreferredSize(new Dimension(800, 600));
		gp.getView().setSize(800, 600);
	}

	public void show() {
		startGeneticAlgorithm(problem);
	}

	private void startGeneticAlgorithm(Problem problem) {	
		Algorithm algorithm = new Algorithm(problem, 0.02, 0.2, 5);
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
			while (running) {
				p.showInfo();
				dataset.addValue(p.getFittest().getFitness(), "population", "" + p.num());
				p = algorithm.evolve(p);
				gp.update(p.getFittest());
				fitnessLabel.setText("" + p.getFittest().getFitness());
				dialog.repaint();
			}
			p.showInfo();
		}

		public void terminate() {
			running = false;
		}
	}
}
