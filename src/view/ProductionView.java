package view;

import model.Buffer;

import javax.swing.*;
import java.awt.*;

public class ProductionView extends JFrame {
    private final JTextArea logArea;
    private final JProgressBar progressBar;
    private final JLabel workerCountLabel;
    private final JLabel consumerCountLabel;
    private final JButton addWorkerBtn;
    private final JButton removeWorkerBtn;
    private final JButton saveStateBtn;
    private final JButton loadStateBtn;
    private final JButton startStopBtn;
    private final JButton clearLogBtn;
    private final JSlider productionRateSlider;
    private final JSlider consumptionRateSlider;

    public ProductionView() {
        setTitle("Production Controller");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Log Area
        logArea = new JTextArea();
        JScrollPane logScrollPane = new JScrollPane(logArea);
        add(logScrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 2));

        addWorkerBtn = new JButton("Add Worker");
        removeWorkerBtn = new JButton("Remove Worker");
        saveStateBtn = new JButton("Save State");
        loadStateBtn = new JButton("Load State");
        startStopBtn = new JButton("Start System");
        clearLogBtn = new JButton("Clear Log");

        controlPanel.add(addWorkerBtn);
        controlPanel.add(removeWorkerBtn);
        controlPanel.add(saveStateBtn);
        controlPanel.add(loadStateBtn);
        controlPanel.add(startStopBtn);
        controlPanel.add(clearLogBtn);

        add(controlPanel, BorderLayout.SOUTH);

        // Status Panel
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(3, 2));

        workerCountLabel = new JLabel("Workers: 0");
        workerCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        consumerCountLabel = new JLabel("Consumers: 0");
        consumerCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        statusPanel.add(workerCountLabel);
        statusPanel.add(consumerCountLabel);
        statusPanel.add(progressBar);

        add(statusPanel, BorderLayout.NORTH);

        // Rate Sliders
        JPanel ratePanel = new JPanel();
        ratePanel.setLayout(new GridLayout(2, 1));

        productionRateSlider = new JSlider(1, 10);
        consumptionRateSlider = new JSlider(1, 10);

        ratePanel.add(new JLabel("Production Rate:"));
        ratePanel.add(productionRateSlider);
        ratePanel.add(new JLabel("Consumption Rate:"));
        ratePanel.add(consumptionRateSlider);

        add(ratePanel, BorderLayout.EAST);

        // Making the frame visible
        setVisible(true);
    }

    // ... Getter and Setter methods for components ...

    public JTextArea getLogArea() {
        return logArea;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getWorkerCountLabel() {
        return workerCountLabel;
    }

    public JLabel getConsumerCountLabel() {
        return consumerCountLabel;
    }

    public JButton getAddWorkerBtn() {
        return addWorkerBtn;
    }

    public JButton getRemoveWorkerBtn() {
        return removeWorkerBtn;
    }

    public JButton getSaveStateBtn() {
        return saveStateBtn;
    }

    public JButton getLoadStateBtn() {
        return loadStateBtn;
    }

    public JButton getStartStopBtn() {
        return startStopBtn;
    }

    public JButton getClearLogBtn() {
        return clearLogBtn;
    }

    public JSlider getProductionRateSlider() {
        return productionRateSlider;
    }

    public JSlider getConsumptionRateSlider() {
        return consumptionRateSlider;
    }
}
