package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Buffer implements Serializable {

    private int availableItems = 0;
    private transient List<Thread> consumerThreads = new ArrayList<>();
    public static final int MAX_BUFFER_SIZE = 100;
    private int totalProducedItems = 0;

    public synchronized void add(Item item) {
        availableItems++;
        totalProducedItems++;  // Increase the total produced count
        notifyAll();  // Notifying any waiting consumers
        System.out.println("Item added. Available items: " + availableItems);
    }

    public synchronized void consume() {
        while (availableItems == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        availableItems--;
        System.out.println("Item consumed. Available items: " + availableItems);
    }

    public int size() {
        return availableItems;
    }

    public void addConsumer() {
        int defaultRate = 5000;  // Default rate of 5 seconds
        Consumer consumer = new Consumer(this, defaultRate);
        Thread consumerThread = new Thread(consumer);
        consumerThreads.add(consumerThread);
        consumerThread.start();
    }


    public int getTotalProducedItems() {
        return totalProducedItems;
    }

    public int getConsumerCount() {
        return consumerThreads.size();
    }

    // This method ensures consumerThreads is initialized after deserialization
    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        consumerThreads = new ArrayList<>();
    }
}
