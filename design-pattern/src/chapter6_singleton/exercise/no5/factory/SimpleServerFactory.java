package chapter6_singleton.exercise.no5.factory;

import chapter6_singleton.exercise.no5.Server;
import chapter6_singleton.exercise.no5.ServerProxy;

public class SimpleServerFactory implements ServerFactory{
    private static SimpleServerFactory INSTANCE = null;

    @Override
    public ServerProxy getServer() {
        return new Server();
    }

    public static SimpleServerFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SimpleServerFactory();
        }
        return INSTANCE;
    }
}
