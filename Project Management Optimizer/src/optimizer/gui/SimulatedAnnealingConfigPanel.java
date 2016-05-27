package optimizer.gui;

import javax.swing.JPanel;

import optimizer.Problem;

public class SimulatedAnnealingConfigPanel extends JPanel {
	private Problem problem;
	public SimulatedAnnealingConfigPanel(Problem problem) {
		this.problem = problem;
	}
}
