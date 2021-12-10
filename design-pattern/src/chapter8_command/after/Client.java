package chapter8_command.after;

import chapter8_command.after.command.AlarmOnCommand;
import chapter8_command.after.command.Command;
import chapter8_command.after.command.LampOffCommand;
import chapter8_command.after.command.LampOnCommand;

public class Client {

    public static void main(String[] args) {
        Lamp lamp = new Lamp();
        Command lampCommand = new LampOnCommand(lamp);

        Button button1 = new Button(lampCommand);
        button1.pressed();

        Alarm alarm = new Alarm();
        Command alarmCommand = new AlarmOnCommand(alarm);

        Button button2 = new Button(alarmCommand);
        button2.pressed();

        button2.setCommand(lampCommand);
        button2.pressed();

        Command lampOffCommand = new LampOffCommand(lamp);
        button2.setCommand(lampOffCommand);
        button2.pressed();
    }
}
