package chapter9_observer.lecture.after;

public class User implements Subscriber{

    private String name;

    public User(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void handleMessage(final String message) {
        System.out.println(message);
    }
}
