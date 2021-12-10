package chapter8_command.before;

public class Button {
    private Lamp theLamp;
    private Alarm theAlarm;
    private Mode mode;

    public Button(final Lamp theLamp, final Alarm theAlarm) {
        this.theLamp = theLamp;
        this.theAlarm = theAlarm;
    }

    public void setMode(final Mode mode) {
        this.mode = mode;
    }

    public void pressed() {
        switch (mode) {
            case LAMP:
                theLamp.turnOn();
                break;
            case ALARM:
                theAlarm.start();
                break;
        }
    }
}
