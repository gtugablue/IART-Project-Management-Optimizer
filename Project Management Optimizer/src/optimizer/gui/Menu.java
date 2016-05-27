package optimizer.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class Menu extends JPanel {
	private Problem problem;
	private JFrame frame;
	private JTabbedPane tabbedPane;
	public Menu(JFrame frame) {
		this.frame = frame;
		
		requestFocus();
		setLayout(new BorderLayout());
		
		problem = Optimizer.generateRandomProblem();
		//problem = Optimizer.loadProblemFromJSON("inputExample.json");
		
        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.addTab("Genetic Algorithm", new GeneticAlgorithmConfigPanel(this.frame, problem));
        this.tabbedPane.addTab("Simulated Annealing", new SimulatedAnnealingConfigPanel(problem));
        
        add(this.tabbedPane);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
