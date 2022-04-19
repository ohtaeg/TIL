package chapter9_observer.lecture.exercise.no1.after;

public interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(final Battery battery);
}
