package model;

import java.util.Random;

public class Producer implements Runnable {
    private final Buffer buffer;
    private boolean running;
    private int totalProduced;
    private final int productionInterval; // Interval at which this producer works

    public Producer(Buffer buffer, int totalProduced) {
        this.buffer = buffer;
        this.totalProduced = totalProduced;
        this.running = true;
        this.productionInterval = calculateProductionInterval();
    }

    private int calculateProductionInterval() {
        // This could be a random value or a fixed rate as per your requirement
        // For example, if you want to randomize the production interval:
        return (new Random().nextInt(10) + 1) * 1000; // Random between 1 and 10 seconds
    }

    @Override
    public void run() {
        while (running) {
            Item item = new Item(Integer.toString(totalProduced++));
            buffer.addItem(item);
            try {
                Thread.sleep(productionInterval); // Sleep for the production interval
            } catch (InterruptedException e) {
                running = false; // If interrupted, stop producing
            }
        }
    }

    public void stop() {
        running = false; // Set running flag too false to stop production
    }

    // Getter for productionInterval for logging purposes
    public int getProductionInterval() {

        return productionInterval / 1000; // Returns in seconds
    }
}
