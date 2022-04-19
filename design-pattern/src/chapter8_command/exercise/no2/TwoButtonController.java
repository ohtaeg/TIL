package chapter8_command.exercise.no2;

public class TwoButtonController {

    private Command command1;
    private Command command2;

    public TwoButtonController(final Command command1, final Command command2) {
        this.command1 = command1;
        this.command2 = command2;
    }

    public void setCommand(final Command command1, final Command command2) {
        this.command1 = command1;
        this.command2 = command2;
    }

    public void button1Pressed() {
        this.command1.execute();
    }

    public void button2Pressed() {
        this.command2.execute();
    }
}
