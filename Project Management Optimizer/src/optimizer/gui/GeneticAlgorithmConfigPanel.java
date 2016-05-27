package optimizer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.NumberFormatter;

import optimizer.Problem;

public class GeneticAlgorithmConfigPanel extends JPanel {
	private Problem problem;
	private JFrame frame;
	private JTextField populationSizeField;
	private JTextField mutationRatioField;
	public GeneticAlgorithmConfigPanel(JFrame frame, Problem problem) {
		this.frame = frame;
		this.problem = problem;
		setLayout(new SpringLayout());
		
		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(false);
		NumberFormat floatFormat = new DecimalFormat("#0.#");
		format.setGroupingUsed(false);
		NumberFormatter formatter;
		
		JLabel l1 = new JLabel("Population size: ");
		add(l1);
		formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
	    formatter.setMinimum(1);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setCommitsOnValidEdit(true);
	    //formatter.setAllowsInvalid(false);
		populationSizeField = new JFormattedTextField(formatter);
		l1.setLabelFor(populationSizeField);
		add(populationSizeField);
		
		JLabel l2 = new JLabel("Mutation rate: ");
		add(l2);
		
		formatter = new NumberFormatter(floatFormat);
		formatter.setValueClass(Float.class);
		formatter.setMinimum(0.0f);
		formatter.setMaximum(1.0f);
		formatter.setCommitsOnValidEdit(true);
		//formatter.setAllowsInvalid(false);
		mutationRatioField = new JFormattedTextField(formatter);
		l2.setLabelFor(mutationRatioField);
		add(mutationRatioField);
		
		JButton runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGeneticAlgorithmDialog(problem);
			}
		});
		add(runButton);
		
		SpringUtilities.makeCompactGrid(this,
                2, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
	}
	
	private void startGeneticAlgorithmDialog(Problem problem) {
		new GeneticAlgorithmDialog(this.frame, problem).show();
	}
}
