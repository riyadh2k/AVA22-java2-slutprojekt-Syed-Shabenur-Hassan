package model;

public class Consumer implements Runnable {
    private final Buffer buffer;
    private boolean running;
    private final int consumptionRate;

    public Consumer(Buffer buffer, int rate) {
        this.buffer = buffer;
        this.consumptionRate = rate;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            buffer.consumeItem();
            // Process the consumed item (if needed)
            try {
                Thread.sleep(consumptionRate); // Sleep for the consumption rate
            } catch (InterruptedException e) {
                running = false; // If interrupted, stop consuming
            }
        }
    }

}
