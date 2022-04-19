package chapter9_observer.lecture.exercise.no1.after;

import java.util.ArrayList;
import java.util.List;

public class Battery implements Subject {

    private int level = 100;
    private List<Observer> observers;

    public Battery() {
        this.observers = new ArrayList<>();
    }

    public Battery(final List<Observer> observers) {
        this.observers = observers;
    }

    public void consume(int amount) {
        this.level -= amount;
        notifyObservers(this);
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void attach(final Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void detach(final Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(final Battery battery) {

        this.observers.forEach(observer -> observer.update(battery));
    }
}
