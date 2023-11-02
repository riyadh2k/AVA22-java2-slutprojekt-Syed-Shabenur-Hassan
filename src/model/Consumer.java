package model;

import java.io.Serializable;

public class Consumer implements Runnable, Serializable {
    private final Buffer buffer;  // Made this final
    private volatile boolean isRunning = true;  // Volatile for thread safety
    private Thread currentThread;
    private int consumptionRate;  // Milliseconds to sleep

    public Consumer(Buffer buffer, int consumptionRate) {
        if (buffer == null) {
            throw new IllegalArgumentException("Buffer cannot be null.");
        }
        this.buffer = buffer;
        this.consumptionRate = consumptionRate;
    }

    @Override
    public void run() {
        currentThread = Thread.currentThread();  // Immediate initialization
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(consumptionRate);
                buffer.consume();
                // Optional: System.out.println("Consumed an item.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Preserve interrupted status
                System.out.println("Consumer was interrupted.");
                isRunning = false;
            }
        }
    }

    public void stop() {
        isRunning = false;
        if (currentThread != null) {
            currentThread.interrupt();
        }
    }

    public void setConsumptionRate(int consumptionRate) {
        this.consumptionRate = consumptionRate;
    }
}
