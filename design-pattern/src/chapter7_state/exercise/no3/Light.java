package chapter7_state.exercise.no3;

import chapter7_state.exercise.no3.state.OFF;
import chapter7_state.exercise.no3.state.PowerSwitch;

public class Light {
    private PowerSwitch state;

    public Light() {
        this(new OFF());
    }

    public Light(PowerSwitch state) {
        this.state = state;
    }

    public void setState(final PowerSwitch powerSwitch) {
        this.state = powerSwitch;
    }

    public void on() {
        state.on(this);
    }

    public void off() {
        state.off(this);
    }
}
