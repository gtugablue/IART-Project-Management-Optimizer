package optimizer.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class GUI {
	private JFrame frame;
	public GUI() {
		frame = new JFrame("Project Management Optimizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
		}
	}	
	public void show() {
		frame.getContentPane().add(new Menu(frame));
		frame.setSize(new Dimension(600, 200));
		frame.setLocationRelativeTo(null);
        display();
    }
	
	void display() {
		frame.pack();
        frame.setVisible(true);
	}
}
