package optimizer.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class GeneticAlgorithm {
	private JFrame frame;
	private Problem problem;
	private GraphPanel gp;
	public GeneticAlgorithm(Problem problem) {
		this.frame = new JFrame("Genetic Algorithm");
		this.problem = problem;
		this.gp = new GraphPanel(problem);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void show() {
		frame.getContentPane().add(gp.getView());
		frame.pack();
		frame.setVisible(true);
		startGeneticAlgorithm(problem);
	}
	
	private void startGeneticAlgorithm(Problem problem) {		
		Algorithm algorithm = new Algorithm(problem, 0.02, 0.2, 5);
		Population population = Optimizer.createStartingPopulation(algorithm);
		gp.update(population.getFittest());
		for (int i = 0; i < 10000; i++) {
			population.showInfo();
			population = algorithm.evolve(population);
			gp.update(population.getFittest());
		}
		population.showInfo();
	}
}
