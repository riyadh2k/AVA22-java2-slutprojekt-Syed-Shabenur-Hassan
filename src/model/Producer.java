package model;

import java.io.Serializable;
import java.util.Random;

public class Producer implements Runnable, Serializable {
    private Buffer buffer;
    private boolean isRunning = true;
    private Random rand = new Random();

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(rand.nextInt(10000) + 1);
                buffer.add(new Item("Item-" + rand.nextInt(100)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.isRunning = false;
    }
}
