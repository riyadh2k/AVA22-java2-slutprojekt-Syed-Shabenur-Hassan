package view;


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
    private JButton startStopBtn;
    private final JButton clearLogBtn;
    private final JSlider productionRateSlider;
    private final JSlider consumptionRateSlider;
    private final JLabel availableItemsLabel;
    private final JLabel consumedItemsLabel;
    private boolean systemRunning = false;  // To keep track of system state

    public ProductionView() {
        setTitle("Production Controller");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        // Initialization should happen in the constructor to ensure that they are always initialized
        availableItemsLabel = new JLabel("Available Items: 0");
        consumedItemsLabel = new JLabel("Consumed Items: 0");

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

        startStopBtn.addActionListener(e -> toggleSystemState());

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
        statusPanel.setLayout(new GridLayout(5, 2));

        workerCountLabel = new JLabel("Workers: 0");
        workerCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        consumerCountLabel = new JLabel("Consumers: 0");
        consumerCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        statusPanel.add(workerCountLabel);
        statusPanel.add(consumerCountLabel);
        statusPanel.add(progressBar);
        statusPanel.add(availableItemsLabel);
        statusPanel.add(consumedItemsLabel);

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
        // Adding listeners for debugging
        addWorkerBtn.addActionListener(e -> System.out.println("Add Worker button clicked."));
        removeWorkerBtn.addActionListener(e -> System.out.println("Remove Worker button clicked."));
        saveStateBtn.addActionListener(e -> System.out.println("Save State button clicked."));
        loadStateBtn.addActionListener(e -> System.out.println("Load State button clicked."));
        startStopBtn.addActionListener(e -> System.out.println("Start/Stop button clicked."));
        clearLogBtn.addActionListener(e -> System.out.println("Clear Log button clicked."));

        // Initialize with red to show that system is off
        updateButtonAppearance();
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
    public JLabel getAvailableItemsLabel() {
        return availableItemsLabel;
    }

    public JLabel getConsumedItemsLabel() {
        return consumedItemsLabel;
    }

    private void updateButtonAppearance() {
        if (systemRunning) {
            startStopBtn.setText("Stop System");
            startStopBtn.setBackground(Color.RED);
        } else {
            startStopBtn.setText("Start System");
            startStopBtn.setBackground(Color.GREEN);
        }
    }

    public boolean isSystemRunning() {
        return systemRunning;
    }

    // Method to toggle system state and update button
    public void toggleSystemState() {
        systemRunning = !systemRunning; // Toggle state
        updateButtonAppearance();  // Use the method to ensure consistent button appearance
    }


}

