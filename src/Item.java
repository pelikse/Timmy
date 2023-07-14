import javax.swing.*;

// A class representing an item in the to-do list
class Item {
    private int id;
    private JLabel data;
    private boolean done;

    public Item(int id, JLabel data, boolean done) {
        this.id = id;
        this.data = data;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data.getText();
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return data.getText();
    }
}