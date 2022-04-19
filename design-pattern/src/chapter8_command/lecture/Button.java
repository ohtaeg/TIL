package chapter8_command.lecture;

import chapter8_command.lecture.command.Command;
import java.util.Stack;

public class Button {
    private Stack<Command> commandHistory;

    public Button() {
        this.commandHistory = new Stack<>();
    }

    public void pressed(Command command) {
        command.execute();
        commandHistory.push(command);
    }

    public void undo() {
        if (!commandHistory.isEmpty()) {
            Command command = commandHistory.pop();
            command.undo();
        }
    }
}