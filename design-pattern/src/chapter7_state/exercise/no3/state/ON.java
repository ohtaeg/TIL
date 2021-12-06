package chapter7_state.exercise.no3.state;

import chapter7_state.exercise.no3.Light;

public class ON implements PowerSwitch {

    @Override
    public void on(final Light light) {
        System.out.println("on -> sleeping");
        light.setState(Sleeping.getInstance());
    }

    @Override
    public void off(final Light light) {
        System.out.println("on -> off");
        light.setState(OFF.getInstance());
    }

    public static PowerSwitch getInstance() {
        return LazyHolder.powerSwitch;
    }

    private static class LazyHolder {
        private static final PowerSwitch powerSwitch = new ON();
    }
}
