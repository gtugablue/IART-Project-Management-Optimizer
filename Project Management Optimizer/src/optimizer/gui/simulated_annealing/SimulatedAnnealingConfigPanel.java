package optimizer.gui.simulated_annealing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.gui.SpringUtilities;
import optimizer.jsonParser;
import optimizer.solver.simulated_annealing.Config;

public class SimulatedAnnealingConfigPanel extends JPanel {
	private JTextField filePathField;
	private JFrame frame;
	private JTextField initialTemperatureField;
	private JTextField coolingRateField;
	public SimulatedAnnealingConfigPanel(JFrame frame, JTextField filePathField) {
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

		DecimalFormat format = new DecimalFormat("0.00000");
		format.setGroupingUsed(false);
		DecimalFormatSymbols custom = new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		format.setDecimalFormatSymbols(custom);
		NumberFormatter formatter;
		
		JLabel l1 = new JLabel("Initial temperature: ", JLabel.TRAILING);
		panel.add(l1);
		formatter = new NumberFormatter(format);
		formatter.setValueClass(Float.class);
		formatter.setMinimum(0.0f);
		formatter.setCommitsOnValidEdit(true);
		initialTemperatureField = new JFormattedTextField(formatter);
		initialTemperatureField.setText("100");
		l1.setLabelFor(initialTemperatureField);
		panel.add(initialTemperatureField);

		JLabel l2 = new JLabel("Cooling rate: ", JLabel.TRAILING);
		panel.add(l2);
		//format = new DecimalFormat("0.00000");
		formatter = new NumberFormatter(format);
		formatter.setValueClass(Double.class);
		formatter.setMinimum(0.0000d);
		formatter.setMaximum(1.0000d);
		formatter.setCommitsOnValidEdit(true);
		coolingRateField = new JFormattedTextField(formatter);
		coolingRateField.setText("0.9999");
		l2.setLabelFor(coolingRateField);
		panel.add(coolingRateField);

		SpringUtilities.makeCompactGrid(panel,
				2, 2, //rows, cols
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
				//Problem problem = Optimizer.generateRandomProblem();
				if (problem == null) {
					JOptionPane.showMessageDialog(panel, "Could not open file", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Config config = new Config();
				config.coolingRate = Double.parseDouble(coolingRateField.getText());
				config.initTemperature = (int) Double.parseDouble(initialTemperatureField.getText());
				startSADialog(problem, config);
			}
		});
		panel.add(runButton);
		return panel;
	}

	private void startSADialog(Problem problem, Config config) {
		new SimulatedAnnealingDialog(this.frame, problem, config).show();
	}
}
