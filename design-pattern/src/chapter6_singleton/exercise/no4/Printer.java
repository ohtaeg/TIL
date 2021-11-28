package chapter6_singleton.exercise.no4;

import java.util.Random;

public class Printer {
    private boolean available = true;

    public void print(String name) {
        try {
            Thread.sleep(new Random().nextInt(100));
            System.out.println(name + " is using " + this.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setAvailable(true);
    }

    public void setAvailable(final boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
}
