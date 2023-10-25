package model;

import java.io.Serializable;
import java.util.Random;

public class Consumer implements Runnable, Serializable {
    private Buffer buffer;
    private boolean isRunning = true;
    private Random rand = new Random();

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(rand.nextInt(10000) + 1);  // Random interval between 1-10 seconds
                buffer.consume();
            } catch (InterruptedException e) {
                isRunning = false;
            }
        }
    }
}

