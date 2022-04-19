package chapter9_observer.lecture.exercise.no2;

public interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(ElevatorController elevatorController);
}
