package chapter8_command.exercise.no1;

public class Tv {

    private boolean powerOn = false;
    private boolean muteOn = false;

    public void power() {
        powerOn = !powerOn;

        if (powerOn) {
            System.out.println("power on");
            return;
        }

        System.out.println("power off");
    }

    public void mute() {
        if (!powerOn) {
            return;
        }

        muteOn = !muteOn;

        if (muteOn) {
            System.out.println("mute on");
            return;
        }
        System.out.println("mute off");
    }
}
