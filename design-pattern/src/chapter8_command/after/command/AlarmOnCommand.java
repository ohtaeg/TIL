package chapter8_command.after.command;

import chapter8_command.after.Alarm;

public class AlarmOnCommand implements Command {

    private Alarm alarm;

    public AlarmOnCommand(final Alarm alarm) {
        this.alarm = alarm;
    }

    @Override
    public void execute() {
        alarm.start();
    }
}
