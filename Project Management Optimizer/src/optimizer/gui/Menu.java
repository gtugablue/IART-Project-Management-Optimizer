package optimizer.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.gui.genetic_algorithm.GeneticAlgorithmConfigPanel;
import optimizer.gui.simulated_annealing.SimulatedAnnealingConfigPanel;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class Menu extends JPanel {
	private Problem problem;
	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JTextField filePathField;
	public Menu(JFrame frame) {
		this.frame = frame;
		
		requestFocus();
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(6, 6, 6, 6));
		setPreferredSize(new Dimension(600, 200));
		
		add(new InputFileChooserPanel(this), BorderLayout.NORTH);
        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.addTab("Genetic Algorithm", new GeneticAlgorithmConfigPanel(this.frame, filePathField));
        this.tabbedPane.addTab("Simulated Annealing", new SimulatedAnnealingConfigPanel(this.frame, filePathField));
        
        add(this.tabbedPane, BorderLayout.CENTER);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	public class InputFileChooserPanel extends JPanel {
		Container parent;
		public InputFileChooserPanel(Container parent) {
			this.parent = parent;
			
			this.setLayout(new BorderLayout());
			
			filePathField = new JTextField();
			
			JButton fileChooserBtn = new JButton("Browse...");
			fileChooserBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.addChoosableFileFilter(new FileFilter() {
						@Override
						public boolean accept(File file) {
							return file.getName().endsWith(".json") || file.isDirectory();
						}

						@Override
						public String getDescription() {
							return "JSON (*.json)";
						}
					});
					fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
					int result = fileChooser.showOpenDialog(frame);
					if (result == JFileChooser.APPROVE_OPTION) {
					    File selectedFile = fileChooser.getSelectedFile();
					    filePathField.setText(selectedFile.getAbsolutePath());
					}
				}
			});
			
			add(new JLabel("Input JSON file: "), BorderLayout.WEST);
			add(filePathField, BorderLayout.CENTER);
			add(fileChooserBtn, BorderLayout.EAST);
		}
	}
}
