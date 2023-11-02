package model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable, Serializable {
    private final Buffer buffer;
    private boolean isRunning = true;
    private static final AtomicInteger counter = new AtomicInteger();
    private Thread currentThread;
    private int productionRate;  // Milliseconds to sleep

    public Producer(Buffer buffer, int productionRate) {
        this.buffer = buffer;
        this.productionRate = productionRate;
    }

    @Override
    public void run() {
        currentThread = Thread.currentThread();
        while (isRunning) {
            try {
                Thread.sleep(productionRate);  // Use the productionRate variable for sleeping
                buffer.add(new Item("Item-" + counter.incrementAndGet()));
                // Optional: System.out.println("Produced: Item-" + counter);
            } catch (InterruptedException e) {
                System.out.println("Producer was interrupted.");
                isRunning = false;  // Handle interruption
                break;
            }
        }
    }

    public void stop() {
        isRunning = false;
        if (currentThread != null) {
            currentThread.interrupt();
        }
    }

    // Setter to adjust the production rate on-the-fly
    public void setProductionRate(int productionRate) {
        this.productionRate = productionRate;
    }
}
