package chapter9_observer.lecture.injava;

import java.util.Observable;
import java.util.Observer;

public class ObserverInJava {

    static class User implements Observer {
        @Override
        public void update(final Observable o, final Object arg) {
            System.out.println(arg);
        }
    }

    static class Subject extends Observable {
        public void add(String message) {
            setChanged();
            notifyObservers(message);
        }
    }

    public static void main(String[] args) {
        Subject subject = new Subject();
        User user = new User();
        subject.addObserver(user);
        subject.add("hello java, observer");
    }

}
