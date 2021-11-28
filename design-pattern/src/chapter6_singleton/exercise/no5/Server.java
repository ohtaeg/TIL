package chapter6_singleton.exercise.no5;

public class Server implements ServerProxy {
    @Override
    public void doSomething() {
        System.out.println("real");
    }
}
