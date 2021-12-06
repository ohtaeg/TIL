package chapter7_state.exercise.no3.state;

import chapter7_state.exercise.no3.Light;

public interface PowerSwitch {

    void on(final Light light);

    void off(final Light light);
}

