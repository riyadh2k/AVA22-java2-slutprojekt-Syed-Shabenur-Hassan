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
import java.util.Timer;
import java.util.TimerTask;

import static model.Buffer.MAX_BUFFER_SIZE;

public class ProductionController {
    private ProductionView view;
    private Buffer buffer;
    private List<Producer> workerList;
    private List<Consumer> consumerList;
    private Timer consumerTimer;


    public ProductionController() {
        this.view = new ProductionView();
        this.buffer = new Buffer();
        this.workerList = new ArrayList<>();
        this.consumerList = new ArrayList<>();

        // Listeners
        view.getAddWorkerBtn().addActionListener(e -> addWorker());
        view.getRemoveWorkerBtn().addActionListener(e -> removeWorker());
        view.getSaveStateBtn().addActionListener(e -> saveState());
        view.getLoadStateBtn().addActionListener(e -> loadState());
        view.getStartStopBtn().addActionListener(e -> toggleSystem());

        // Start the progress bar and log updater
        startProgressBarUpdater();
        startConsumerTimer();
    }

    public void start() {
        view.show();
    }

    private void startProgressBarUpdater() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int bufferPercentage = (buffer.size() * 100) / Buffer.MAX_BUFFER_SIZE;

                // Logic to automatically adjust workers and consumers
                if (bufferPercentage > 60 && !workerList.isEmpty()) {
                    removeWorker();
                } else if (bufferPercentage < 40) {
                    addWorker();
                }

                // Update the progress bar
                JProgressBar progressBar = view.getProgressBar();
                progressBar.setValue(bufferPercentage);
                progressBar.setForeground(bufferPercentage <= 10 ? Color.RED : bufferPercentage >= 90 ? Color.GREEN : Color.BLUE);
                view.getAvailableItemsLabel().setText("Available Items: " + buffer.size());
                view.getConsumedItemsLabel().setText("Consumed Items: " + (buffer.getTotalProducedItems() - buffer.size()));
            }
        }, 0, 1000);  // Update every second
    }


    private void startConsumerTimer() {
        int defaultConsumptionRate = 5000;  // Default rate of 5 seconds
        consumerTimer = new Timer();
        consumerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Consumer consumer = new Consumer(buffer, defaultConsumptionRate);
                consumerList.add(consumer);
                Thread consumerThread = new Thread(consumer);
                consumerThread.start();
            }
        }, 0, 60000);  // Add a consumer every minute
    }


    private void addWorker() {
        int defaultProductionRate = 2000;  // Default rate of 2 seconds
        Producer producer = new Producer(buffer, defaultProductionRate);
        workerList.add(producer);
        Thread producerThread = new Thread(producer);
        producerThread.start();

        // Update the worker count in the view
        view.getWorkerCountLabel().setText("Workers: " + workerList.size());

        view.getLogArea().append("Added a worker.\n");
        System.out.println("Worker added. Total workers: " + workerList.size());
    }


    private void removeWorker() {
        if (!workerList.isEmpty()) {
            Producer workerToRemove = workerList.remove(workerList.size() - 1);
            workerToRemove.stop();

            // Update the worker count in the view
            view.getWorkerCountLabel().setText("Workers: " + workerList.size());

            view.getLogArea().append("Removed a worker.\n");
        }
        System.out.println("Worker removed. Total workers: " + workerList.size());
    }
    private void addConsumer() {
        buffer.addConsumer();
        view.getConsumerCountLabel().setText("Consumers: " + buffer.getConsumerCount());
        view.getLogArea().append("Added a consumer.\n");
    }

    private void saveState() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("state.obj"))) {
            out.writeObject(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("State saved successfully.");
    }

    private void loadState() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("state.obj"))) {
            toggleSystem(); // Turn off the system to ensure all ongoing threads are stopped
            buffer = (Buffer) in.readObject();
            // Update the view based on the loaded buffer's state
            view.getAvailableItemsLabel().setText("Available Items: " + buffer.size());
            view.getConsumedItemsLabel().setText("Consumed Items: " + (buffer.getTotalProducedItems() - buffer.size()));
            view.getWorkerCountLabel().setText("Workers: 0");
            view.getConsumerCountLabel().setText("Consumers: " + buffer.getConsumerCount());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("State loaded successfully. Buffer size: " + buffer.size());
    }

    private void toggleSystem() {
        if (view.isSystemRunning()) {
            // Logic to start the system

            // Start the progress bar and log updater
            startProgressBarUpdater();
            startConsumerTimer();

            // Optionally, you can add initial workers or consumers here
            addWorker();
            addConsumer();

            view.getLogArea().append("System started.\n");

        } else {
            // Logic to stop the system

            // Stopping the progress bar and log updater timer tasks
            if(consumerTimer != null) {
                consumerTimer.cancel();
                consumerTimer = null;
            }

            // Optionally, you can stop all workers and consumers
            for(Producer worker : workerList) {
                worker.stop();
            }
            workerList.clear();

            for(Consumer consumer : consumerList) {
                // Assuming you have a stop() method in Consumer class
                consumer.stop();
            }
            consumerList.clear();

            view.getLogArea().append("System stopped.\n");
        }
    }



}