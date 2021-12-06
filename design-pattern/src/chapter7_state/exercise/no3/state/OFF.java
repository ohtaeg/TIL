package chapter7_state.exercise.no3.state;

import chapter7_state.exercise.no3.Light;

public class OFF implements PowerSwitch {

    @Override
    public void on(final Light light) {
        System.out.println("Light On");
        light.setState(ON.getInstance());
    }

    @Override
    public void off(final Light light) {
        new UnsupportedOperationException("잘못 누르셨음");
    }

    public static PowerSwitch getInstance() {
        return LazyHolder.powerSwitch;
    }

    private static class LazyHolder {
        private static final PowerSwitch powerSwitch = new OFF();
    }
}
