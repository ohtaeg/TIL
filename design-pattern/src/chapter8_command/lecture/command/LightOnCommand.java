package chapter8_command.lecture.command;

import chapter8_command.lecture.Light;

public class LightOnCommand implements Command{

    private Light light;

    public LightOnCommand(final Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        new LightOffCommand(this.light).execute();
    }
}
