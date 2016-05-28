package optimizer.gui.genetic_algorithm;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.NumberFormatter;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.jsonParser;
import optimizer.gui.SpringUtilities;
import optimizer.solver.genetic_algorithm.Config;

public class GeneticAlgorithmConfigPanel extends JPanel {
	private JTextField filePathField;
	private JFrame frame;
	private JTextField populationSizeField;
	private JTextField mutationRateField;
	private JTextField crossoverRateField;
	private JTextField elitismField;
	public GeneticAlgorithmConfigPanel(JFrame frame, JTextField filePathField) {
		this.frame = frame;
		this.filePathField = filePathField;

		createForm();
	}

	private void createForm() {
		this.setLayout(new GridBagLayout());
		JPanel formFields = createFormFields();
		JPanel formSubmit = createFormSubmit();
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		this.add(formFields, c);
		c.gridy = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(formSubmit, c);
	}

	private JPanel createFormFields() {
		JPanel panel = new JPanel();
		panel.setLayout(new SpringLayout());

		DecimalFormat format = new DecimalFormat();
		format.setGroupingUsed(false);
		DecimalFormatSymbols custom = new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		format.setDecimalFormatSymbols(custom);
		NumberFormatter formatter;

		JLabel l1 = new JLabel("Population size: ");
		panel.add(l1);
		formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(1);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setCommitsOnValidEdit(true);
		populationSizeField = new JFormattedTextField(formatter);
		populationSizeField.setText("50");
		l1.setLabelFor(populationSizeField);
		panel.add(populationSizeField);

		JLabel l2 = new JLabel("Mutation coolingRate: ", JLabel.TRAILING);
		panel.add(l2);
		formatter = new NumberFormatter(format);
		formatter.setValueClass(Float.class);
		formatter.setMinimum(0.0f);
		formatter.setMaximum(1.0f);
		formatter.setCommitsOnValidEdit(true);
		mutationRateField = new JFormattedTextField(formatter);
		mutationRateField.setText("0.02");
		l2.setLabelFor(mutationRateField);
		panel.add(mutationRateField);

		JLabel l3 = new JLabel("Crossover coolingRate: ", JLabel.TRAILING);
		panel.add(l3);
		crossoverRateField = new JFormattedTextField(formatter);
		crossoverRateField.setText("0.20");
		l3.setLabelFor(crossoverRateField);
		panel.add(crossoverRateField);

		JLabel l4 = new JLabel("Elitism: ", JLabel.TRAILING);
		panel.add(l4);
		formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setCommitsOnValidEdit(true);
		elitismField = new JFormattedTextField(formatter);
		elitismField.setText("5");
		l4.setLabelFor(elitismField);
		panel.add(elitismField);

		SpringUtilities.makeCompactGrid(panel,
				4, 2, //rows, cols
				6, 6,        //initX, initY
				6, 6);       //xPad, yPad
		return panel;
	}

	private JPanel createFormSubmit() {
		JPanel panel = new JPanel();
		JButton runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Problem problem = jsonParser.parse(filePathField.getText());
				problem = Optimizer.generateRandomProblem();
				if (problem == null) {
					JOptionPane.showMessageDialog(panel, "Could not open file", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Config config = new Config();
				config.populationSize = Integer.parseInt(populationSizeField.getText());
				config.mutationRate = Float.parseFloat(mutationRateField.getText());
				config.crossoverRate = Float.parseFloat(crossoverRateField.getText());
				config.elitism = Integer.parseInt(elitismField.getText());
				startGeneticAlgorithmDialog(problem, config);
			}
		});
		panel.add(runButton);
		return panel;
	}

	private void startGeneticAlgorithmDialog(Problem problem, Config config) {
		new GeneticAlgorithmDialog(this.frame, problem, config).show();
	}
}
