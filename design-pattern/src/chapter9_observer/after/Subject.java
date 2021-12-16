package chapter9_observer.after;

import java.util.ArrayList;
import java.util.List;

// 추상화된 변경 관심 대상 데이터
public abstract class Subject {
    private List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        this.observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
