package chapter8_command.after;

import chapter8_command.after.command.Command;

public class Button {
    private Command command;

    public Button(Command command) {
        this.command = command;
    }

    public void setCommand(final Command command) {
        this.command = command;
    }

    public void pressed() {
        command.execute();
    }
}