package chapter9_observer.lecture.exercise.no2;


import java.util.ArrayList;
import java.util.List;

public class ElevatorController implements Subject {

    private int current = 1;
    private List<Observer> observers;

    public ElevatorController() {
        this.observers = new ArrayList<>();
    }

    public ElevatorController(final List<Observer> observers) {
        this.observers = observers;
    }

    public void gotoFloor(int destination) {
        this.current = destination;
        notifyObservers(this);
    }

    public int getCurrent() {
        return current;
    }

    @Override
    public void attach(final Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void detach(final Observer observer) {
        if (this.observers.contains(observer)) {
            this.observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers(final ElevatorController elevatorController) {
        this.observers.forEach(observer -> observer.update(elevatorController));
    }
}
