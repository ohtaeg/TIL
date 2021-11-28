package chapter6_singleton.exercise.no5;

import chapter6_singleton.exercise.no5.factory.SimpleServerFactory;

public class UseServer {
    public void doSomething() {
        final SimpleServerFactory factory = SimpleServerFactory.getInstance();
        final ServerProxy server = factory.getServer();
        server.doSomething();
    }
}
