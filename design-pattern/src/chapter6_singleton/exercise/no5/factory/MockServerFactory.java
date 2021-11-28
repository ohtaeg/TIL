package chapter6_singleton.exercise.no5.factory;

import chapter6_singleton.exercise.no5.MockServer;
import chapter6_singleton.exercise.no5.Server;
import chapter6_singleton.exercise.no5.ServerProxy;

public class MockServerFactory implements ServerFactory{
    private static MockServerFactory INSTANCE = null;

    @Override
    public ServerProxy getServer() {
        return new MockServer();
    }

    public static MockServerFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockServerFactory();
        }
        return INSTANCE;
    }
}
