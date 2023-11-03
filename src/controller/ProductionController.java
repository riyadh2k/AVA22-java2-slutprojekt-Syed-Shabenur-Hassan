package controller;

import model.Buffer;
import model.Consumer;
import model.Producer;
import view.ProductionView;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ProductionController {
    private final ProductionView view;
    private Buffer buffer;
    private final List<Producer> workerList;
    private final List<Consumer> consumerList;
    private Timer consumerTimer;
    private final Random random = new Random();
    private Timer balanceTimer; // Timer to check and balance the production

    private BufferedWriter logWriter;
    private final String LOG_FILE_NAME = "production_log.txt";

    public ProductionController() {
        this.view = new ProductionView();
        this.buffer = new Buffer();
        this.workerList = new ArrayList<>();
        this.consumerList = new ArrayList<>();
        int rate = 5000; // example rate in milliseconds
        try {
            logWriter = new BufferedWriter(new FileWriter(LOG_FILE_NAME, true)); // true for appending
        } catch (IOException e) {
            e.printStackTrace();
        }

        view.getAddWorkerBtn().addActionListener(e -> addWorker());
        view.getRemoveWorkerBtn().addActionListener(e -> removeWorker());
        view.getSaveStateBtn().addActionListener(e -> saveState());
        view.getLoadStateBtn().addActionListener(e -> loadState());
        view.getStartStopBtn().addActionListener(e -> toggleSystem());

        // Start the progress bar and log updater
        startProgressBarUpdater();
        startConsumerTimer();
        startBalancingTimer();
    }

    public void start() {

        view.show();
    }
    private void startBalancingTimer() {
        balanceTimer = new Timer();
        balanceTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                balanceProduction();
            }
        }, 0, 1000); // Check every second
    }
    private void balanceProduction() {
        int bufferPercentage = (buffer.size() * 100) / Buffer.MAX_BUFFER_SIZE;
        // 45% of buffer capacity
        int LOWER_THRESHOLD = 45;
        // 55% of buffer capacity
        int UPPER_THRESHOLD = 55;
        if (bufferPercentage < LOWER_THRESHOLD && !workerList.isEmpty()) {
            addWorker(); // Add worker if below LOWER_THRESHOLD
        } else if (bufferPercentage > UPPER_THRESHOLD && !workerList.isEmpty()) {
            removeWorker(); // Remove worker if above UPPER_THRESHOLD
        }
    }

    private void startProgressBarUpdater() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int bufferPercentage = (buffer.size() * 100) / Buffer.MAX_BUFFER_SIZE;

                if (bufferPercentage <= 10) {
                    logEvent("Warning: Available units are too low (10% or below).");
                } else if (bufferPercentage >= 90) {
                    logEvent("Warning: Available units are too high (90% or above).");
                }

                JProgressBar progressBar = view.getProgressBar();
                progressBar.setValue(bufferPercentage);
                progressBar.setForeground(bufferPercentage <= 10 ? Color.RED : bufferPercentage >= 90 ? Color.GREEN : Color.BLUE);
                view.getAvailableItemsLabel().setText("Available Items: " + buffer.size());
                view.getConsumedItemsLabel().setText("Consumed Items: " + (buffer.getTotalProducedItems() - buffer.size()));
            }
        }, 0, 1000);  // Update every second
    }

    private void startConsumerTimer() {
        consumerTimer = new Timer();
        consumerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Only add a new consumer if there are less than 15
                if (consumerList.size() < 15 && random.nextInt(6) + 1 == 1) {
                    addConsumer();
                }
            }
        }, 0, 1000);  // Checks every second, but only adds consumer with 1/6 probability
    }


    private void addWorker() {
        Producer producer = new Producer(buffer, buffer.getTotalProducedItems());
        workerList.add(producer);
        Thread producerThread = new Thread(producer);
        producerThread.start();
        view.getWorkerCountLabel().setText("Workers: " + workerList.size());
        logEvent("Added a worker. Total workers: " + workerList.size() + ". Production interval: " + producer.getProductionInterval() + " seconds.");
    }

    private void removeWorker() {
        if (!workerList.isEmpty()) {
            Producer workerToRemove = workerList.remove(workerList.size() - 1);
            workerToRemove.stop();
            Thread.currentThread().interrupt(); // Interrupt the thread to stop immediately
            view.getWorkerCountLabel().setText("Workers: " + workerList.size());
            logEvent("Removed a worker. Total workers: " + workerList.size() + ".");
        }
    }

    private void addConsumer() {
        if (consumerList.size() < 15) { // Ensure we don't add more than 15 consumers
            int consumeTime = random.nextInt(10) + 1; // Random consume time between 1-10 seconds
            Consumer consumer = new Consumer(buffer, consumeTime * 1000); // Convert to milliseconds
            consumerList.add(consumer);
            Thread consumerThread = new Thread(consumer);
            consumerThread.start();
            view.getConsumerCountLabel().setText("Consumers: " + consumerList.size()); // Remove to hide consumer count from user
            logEvent("Added a consumer.");
        }
    }

    private void saveState() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("state.obj"))) {
            out.writeObject(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logEvent("State saved successfully.");
    }

    private void loadState() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("state.obj"))) {
            buffer = (Buffer) in.readObject();
            view.getAvailableItemsLabel().setText("Available Items: " + buffer.size());
            view.getConsumedItemsLabel().setText("Consumed Items: " + (buffer.getTotalProducedItems() - buffer.size()));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        logEvent("State loaded successfully. Buffer size: " + buffer.size());
    }
    private void startBalanceTimer() {
        balanceTimer = new Timer();
        balanceTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                adjustWorkers();
            }
        }, 0, 1000); // Adjust workers every second, or adjust as needed
    }
    private synchronized void adjustWorkers() {
        int bufferPercentage = (buffer.size() * 100) / Buffer.MAX_BUFFER_SIZE;
        if (bufferPercentage < 45) {
            // Add worker if buffer is less than 45% full
            addWorker();
        } else if (bufferPercentage > 55) {
            // Remove worker if the buffer is more than 55% full
            removeWorker();
        }
        // If within 45-55%, no action needed
    }

    private void toggleSystem() {
        if (!view.isSystemRunning()) {
            // Start the system only if it's currently stopped.
            view.setSystemRunning(); // You need to make sure this method exists and correctly updates the state
            startProgressBarUpdater();
            startConsumerTimer();
            startBalanceTimer(); // Start the balancing timer
            logEvent("System started.");
        } else {
            // Stop the system only if it's currently running.
            view.setSystemRunning(); // You need to make sure this method exists and correctly updates the state
            if (consumerTimer != null) {
                consumerTimer.cancel();
                consumerTimer = null;
            }
            if (balanceTimer != null) {
                balanceTimer.cancel(); // Stop the balancing timer
                balanceTimer = null;
            }
            // Stop all workers and consumers
            logEvent("System stopped.");
        }
    }




    private synchronized void logEvent(String event) {
        try {
            logWriter.write(event);
            logWriter.newLine();
            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ensure that the update to the log area is done on the EDT
        SwingUtilities.invokeLater(() -> {
            view.getLogArea().append(event + "\n");
        });
    }


}
