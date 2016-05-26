package optimizer.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class Menu extends JPanel {
	private Problem problem;
	private JFrame frame;
	public Menu(JFrame frame) {
		this.frame = frame;
		
		requestFocus();
		setLayout(new FlowLayout());
		
		problem = Optimizer.generateRandomProblem();
		//problem = Optimizer.loadProblemFromJSON("inputExample.json");
		
		JButton geneticAlgorithmButton = new JButton("Genetic Algorithm");
		geneticAlgorithmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGeneticAlgorithmFrame(problem);
			}
		});
        add(geneticAlgorithmButton);
        
        JButton simulatedAnnealingButton = new JButton("Simulated Annealing");
        simulatedAnnealingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startSimulatedAnnealingFrame(problem);
			}
        });
        add(simulatedAnnealingButton);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	private void startGeneticAlgorithmFrame(Problem problem) {
		new GeneticAlgorithmDialog(this.frame, problem).show();
	}
	
	private void startSimulatedAnnealingFrame(Problem problem) {
		
	}
}
