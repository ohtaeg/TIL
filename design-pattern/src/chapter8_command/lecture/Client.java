package chapter8_command.lecture;

import chapter8_command.lecture.command.GameStartCommand;
import chapter8_command.lecture.command.LightOnCommand;

public class Client {

    public static void main(String[] args) {
        Button button = new Button();
        button.pressed(new GameStartCommand(new Game()));
        button.pressed(new LightOnCommand(new Light()));

        System.out.println("undo");
        button.undo();
        button.undo();
    }
}
