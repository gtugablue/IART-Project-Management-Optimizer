package optimizer.gui.simulated_annealing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.NumberFormatter;

import optimizer.Problem;
import optimizer.jsonParser;
import optimizer.gui.SpringUtilities;
import optimizer.solver.genetic_algorithm.Config;

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

		DecimalFormat format = new DecimalFormat();
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
		formatter = new NumberFormatter(format);
		formatter.setValueClass(Float.class);
		formatter.setMinimum(0.0f);
		formatter.setMaximum(1.0f);
		formatter.setCommitsOnValidEdit(true);
		coolingRateField = new JFormattedTextField(formatter);
		coolingRateField.setText("0.01");
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
				// TODO
			}
		});
		panel.add(runButton);
		return panel;
	}
}
