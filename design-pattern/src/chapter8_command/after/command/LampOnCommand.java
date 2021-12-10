package chapter8_command.after.command;

import chapter8_command.after.Lamp;

public class LampOnCommand implements Command {

    private Lamp lamp;

    public LampOnCommand(final Lamp lamp) {
        this.lamp = lamp;
    }

    @Override
    public void execute() {
        lamp.turnOn();
    }
}
