package optimizer.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class GeneticAlgorithmDialog {
	private JDialog dialog;
	private Problem problem;
	private GraphPanel gp;
	private GeneticAlgoThread algoThread;
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
	}

	public void show() {
		dialog.setLayout(new FlowLayout());
		dialog.add(gp.getView());
		dialog.setModal(false);
		gp.getView().setPreferredSize(new Dimension(800, 600));
		gp.getView().setSize(800, 600);
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
				p = algorithm.evolve(p);
				gp.update(p.getFittest());
				dialog.repaint();
			}
			p.showInfo();
		}
		
		public void terminate() {
			running = false;
		}
	}
}
