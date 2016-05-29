package optimizer.gui.simulated_annealing;

import optimizer.Problem;
import optimizer.Solution;
import optimizer.domain.Element;
import optimizer.gui.GraphPanel;
import optimizer.solver.simulated_annealing.Algorithm;
import optimizer.solver.simulated_annealing.Config;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by duarte on 29-05-2016.
 */
public class SimulatedAnnealingDialog {
    private JDialog dialog;
    private Problem problem;
    private Config config;
    private GraphPanel gp;
    private SAThread algoThread;
    private JLabel temperatureLabel;
    private JLabel currValueLabel;
    private JLabel bestValueLabel;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private DefaultCategoryDataset dataset;
    private volatile int selectedTask;
    private DefaultListModel<String> elementListModel;

    public SimulatedAnnealingDialog(JFrame frame, Problem problem, Config config) {
        this.dialog = new JDialog(frame, "Simulated Annealing");
        this.problem = problem;
        this.config = config;
        this.gp = new GraphPanel(problem);
        dialog.setMaximumSize(new Dimension());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (algoThread != null)
                    algoThread.terminate();
            }
        });
        dialog.getContentPane().setLayout(new GridBagLayout());
        temperatureLabel = new JLabel("0");
        currValueLabel = new JLabel("");
        bestValueLabel = new JLabel("");
        dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createLineChart(
                "Cooling Rate",
                "Current value",
                "Best value",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        chartPanel = new ChartPanel(chart);

        JPanel bestInfo = new JPanel();
        bestInfo.setLayout(new FlowLayout());

        bestInfo.add(new JLabel("Temperature:"));
        bestInfo.add(temperatureLabel);

        bestInfo.add(new JLabel("Current Value:"));
        bestInfo.add(currValueLabel);

        bestInfo.add(new JLabel("Best value:"));
        bestInfo.add(bestValueLabel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.weightx = 0.5;
        c.weighty = 0;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        dialog.add(bestInfo, c);
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        dialog.add(chartPanel, c);
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.3;
        dialog.add(createElementsPanel(), c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 3;
        dialog.add(gp.getView(), c);
        dialog.setModal(false);
        gp.getView().setMinimumSize(new Dimension(800, 600));
        gp.getView().setPreferredSize(new Dimension(800, 600));
    }

    private Container createElementsPanel() {
        String[] taskNames = new String[problem.getTasks().size()];
        for (int i = 0; i < problem.getTasks().size(); i++) {
            taskNames[i] = problem.getTasks().get(i).getName();
        }
        JComboBox<String> elementComboBox = new JComboBox<String>(taskNames);
        elementComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTask = elementComboBox.getSelectedIndex();
            }
        });
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        JLabel taskLabel = new JLabel("Task: ");
        elementListModel = new DefaultListModel<String>();
        JList<String> elementList = new JList<String>(elementListModel);
        top.add(taskLabel);
        top.add(elementComboBox);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(elementList);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(top, BorderLayout.NORTH);
        panel.add(scrollPane);
        return panel;
    }

    public void show() {
        startSA(problem, config);
    }

    private void startSA(Problem problem, Config config) {
        Algorithm algorithm = new Algorithm(problem, config);
        gp.update(algorithm.getSchedule());
        dialog.pack();
        dialog.setVisible(true);
        algoThread = new SAThread(algorithm);
        algoThread.start();
    }

    public class SAThread extends Thread {
        private volatile boolean running = false;
        private Algorithm algorithm;
        public SAThread(Algorithm algorithm) {
            this.algorithm = algorithm;
        }
        @Override
        public void run() {
            running = true;
            int oldFitness = (int) algorithm.getScore();
            while (running) {
                Solution s = algorithm.getSchedule();
                chart.setNotify(false);
                if (algorithm.getScore() != oldFitness)
                {
                    chart.setNotify(true);
                    oldFitness = (int) algorithm.getScore();
                }
                dataset.addValue((float)algorithm.getScore(), "current score" , "" + algorithm.getI());
                dataset.addValue(algorithm.getBestAll(), "best score", "" + algorithm.getI());

                elementListModel.clear();
                for (Element e : algorithm.getBestAllSchedule().getTaskAssignedElements(problem.getTasks().get(selectedTask)))
                    elementListModel.addElement(e.getName());

                algorithm.newIteration();
                gp.update(algorithm.getSchedule());
                temperatureLabel.setText("" + algorithm.getTemperature());
                currValueLabel.setText("" + algorithm.getScore());
                bestValueLabel.setText("" + algorithm.getBestAll());
                dialog.repaint();
            }
        }

        public void terminate() {
            running = false;
        }
    }
}
