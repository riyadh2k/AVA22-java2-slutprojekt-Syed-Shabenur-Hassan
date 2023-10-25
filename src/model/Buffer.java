package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Buffer implements Serializable {

    private int availableItems = 0;
    private List<Thread> workerThreads = new ArrayList<>();
    private List<Thread> consumerThreads = new ArrayList<>();


    public synchronized void add(Item item) {
        availableItems++;
        notifyAll();  // Notifying any waiting consumers
    }

    public synchronized void consume() {
        while (availableItems <= 0) {
            try {
                wait();  // If no items are available, consumer waits
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        availableItems--;
    }

    public void addWorker() {
        Producer producer = new Producer(this);
        Thread workerThread = new Thread(producer);
        workerThreads.add(workerThread);
        workerThread.start();
    }

    public void removeWorker() {
        if (!workerThreads.isEmpty()) {
            Thread workerThread = workerThreads.remove(workerThreads.size() - 1);
            workerThread.interrupt();
        }
    }
    public int size() {
        return availableItems;
    }

    public void addConsumer() {
        Consumer consumer = new Consumer(this);
        Thread consumerThread = new Thread(consumer);
        consumerThreads.add(consumerThread);
        consumerThread.start();
    }

    public void removeConsumer() {
        if (!consumerThreads.isEmpty()) {
            Thread consumerThread = consumerThreads.remove(consumerThreads.size() - 1);
            consumerThread.interrupt();
        }
    }

    public int getWorkerCount() {
        return workerThreads.size();
    }

    public int getConsumerCount() {
        return consumerThreads.size();
    }
}