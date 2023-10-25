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
                int bufferPercentage = (buffer.size() * 100) / 100;  // Assuming max capacity is 100
                JProgressBar progressBar = view.getProgressBar();
                progressBar.setValue(bufferPercentage);
                progressBar.setForeground(bufferPercentage <= 10 ? Color.RED : bufferPercentage >= 90 ? Color.GREEN : Color.BLUE);
            }
        }, 0, 1000);  // Update every second
    }

    private void startConsumerTimer() {
        consumerTimer = new Timer();
        consumerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Consumer consumer = new Consumer(buffer);
                consumerList.add(consumer);
                Thread consumerThread = new Thread(consumer);
                consumerThread.start();
            }
        }, 0, 60000);  // Add a consumer every minute
    }

    private void addWorker() {
        Producer producer = new Producer(buffer);
        workerList.add(producer);
        Thread producerThread = new Thread(producer);
        producerThread.start();

        // Update the worker count in the view
        view.getWorkerCountLabel().setText("Workers: " + workerList.size());

        view.getLogArea().append("Added a worker.\n");
    }


    private void removeWorker() {
        if (!workerList.isEmpty()) {
            Producer workerToRemove = workerList.remove(workerList.size() - 1);
            workerToRemove.stop();

            // Update the worker count in the view
            view.getWorkerCountLabel().setText("Workers: " + workerList.size());

            view.getLogArea().append("Removed a worker.\n");
        }
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
    }

    private void loadState() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("state.obj"))) {
            buffer = (Buffer) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
