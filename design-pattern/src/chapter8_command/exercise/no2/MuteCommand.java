package chapter8_command.exercise.no2;

public class MuteCommand implements Command {

    private Tv tv;

    public MuteCommand(final Tv tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.mute();
    }
}
