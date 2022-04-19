package chapter8_command.exercise.no2;

public class PowerControlCommand implements Command {

    private Tv tv;

    public PowerControlCommand(final Tv tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.power();
    }
}
