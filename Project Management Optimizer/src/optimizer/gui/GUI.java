package optimizer.gui;

import javax.swing.JFrame;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class GUI {
	private JFrame frame;
	public GUI() {
		frame = new JFrame("Project Management Optimizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	
	public void show() {
		frame.getContentPane().add(new Menu(frame));
		
        display();
    }
	
	void display() {
		frame.pack();
        frame.setVisible(true);
	}
}
