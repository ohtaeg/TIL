package chapter8_command.after.command;

import chapter8_command.after.Lamp;

public class LampOffCommand implements Command {
    private Lamp lamp;

    public LampOffCommand(final Lamp lamp) {
        this.lamp = lamp;
    }

    @Override
    public void execute() {
        lamp.turnOff();
    }
}
