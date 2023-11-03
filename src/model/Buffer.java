package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class Buffer implements Serializable {
    @Serial
    private static final long serialVersionUID = -6330816751337201597L;
    private transient Queue<Item> items;
    private int totalProducedItems;
    public static final int MAX_BUFFER_SIZE = 100; // Assuming the buffer has a max size

    public Buffer() {
        items = new LinkedList<>();
    }

    public synchronized void addItem(Item item) {
        while (items.size() == MAX_BUFFER_SIZE) {
            try {
                wait(); // Buffer is full, wait for a consumer to consume an item
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Reset the interrupt flag
                return;
            }
        }
        items.add(item);
        totalProducedItems++;
        notifyAll(); // Notify consumers that an item has been added
    }

    public synchronized Item consumeItem() {
        while (items.isEmpty()) {
            try {
                wait(); // Buffer is empty, wait for a producer to add an item
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Reset the interrupt flag
                return null;
            }
        }
        Item item = items.poll();
        notifyAll(); // Notify producers that an item has been consumed
        return item;
    }

    public synchronized int size() {
        return items.size();
    }

    public int getTotalProducedItems() {
        return totalProducedItems;
    }

    // Custom serialization method to handle the transient 'items' field
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        items = new LinkedList<>(); // Initialize the queue upon deserialization
    }


}
