package model;

import java.io.Serializable;

public class Item implements Serializable {
    private String id;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
