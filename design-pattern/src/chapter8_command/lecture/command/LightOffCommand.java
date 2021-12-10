package chapter8_command.lecture.command;

import chapter8_command.lecture.Light;

public class LightOffCommand implements Command{

    private Light light;

    public LightOffCommand(final Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        new LightOnCommand(this.light).execute();
    }
}
