package optimizer.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class GUI {
	private JFrame frame;
	public GUI() {
		frame = new JFrame("Project Management Optimizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
	}	
	public void show() {
        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);
        
        Problem problem = Optimizer.generateRandomProblem();
        solveWithGeneticAlgorithm(problem);
    }
	
	private void solveWithGeneticAlgorithm(Problem problem) {
		GraphPanel gp = new GraphPanel(problem);
		frame.getContentPane().add(gp.getView());
		display();
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
	
	private void display() {
		frame.pack();
        frame.setVisible(true);
	}
}
