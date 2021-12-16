package chapter9_observer.lecture.injava;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PropertyChangeExample {
    static class User implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            System.out.println(evt.getPropertyName());
            System.out.println(evt.getNewValue());
        }
    }

    static class Subject {
        PropertyChangeSupport support = new PropertyChangeSupport(this);

        public void addObserver(PropertyChangeListener observer) {
            support.addPropertyChangeListener(observer);
            // support.addPropertyChangeListener("zz", observer); "zz"라는 특정 property가 변경 되었을 때만 observer 구독할 수 있도록
        }

        public void removeObserver(PropertyChangeListener observer) {
            support.removePropertyChangeListener(observer);
        }

        public void add(String message) {
            support.firePropertyChange("eventName", "legacy", message);
            // support.fireIndexedPropertyChange("eventName", 1, null, message); 우선순위 부여 가능
        }
    }

    public static void main(String[] args) {
        Subject subject = new Subject();
        User observer = new User();
        subject.addObserver(observer);
        subject.add("자바 PCL 예제 코드");
        subject.removeObserver(observer);
        subject.add("이 메시지는 이제 볼 수 없즤");
    }
}
