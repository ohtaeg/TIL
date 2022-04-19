package chapter8_command.exercise.no3;

import chapter8_command.exercise.no3.command.Command;

public class ElevatorButton {

    private Command command;

    public ElevatorButton(final Command command) {
        this.command = command;
    }

    public void press() {
        command.execute();
    }
}
