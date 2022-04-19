package chapter8_command.exercise.no1;

public class TwoButtonController {
    private Tv tv;

    public TwoButtonController(final Tv tv) {
        this.tv = tv;
    }



    public void button1Pressed() {
        tv.power();
    }

    public void button2Pressed() {
        tv.mute();
    }
}
