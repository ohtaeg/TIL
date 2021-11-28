package chapter6_singleton.exercise.no5;

public class MockServer implements ServerProxy{
    @Override
    public void doSomething() {
        System.out.println("mock");
    }
}
