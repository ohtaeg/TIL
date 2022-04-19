package chapter7_state.exercise.no3.state;

import chapter7_state.exercise.no3.Light;

public class Sleeping implements PowerSwitch {

    @Override
    public void on(final Light light) {
        System.out.println("sleeping -> on");
        light.setState(ON.getInstance());
    }

    @Override
    public void off(final Light light) {
        System.out.println("sleeping -> off");
        light.setState(OFF.getInstance());
    }

    public static PowerSwitch getInstance() {
        return LazyHolder.powerSwitch;
    }

    private static class LazyHolder {
        private static final PowerSwitch powerSwitch = new Sleeping();
    }
}
