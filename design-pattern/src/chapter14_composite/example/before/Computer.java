package chapter14_composite.example.before;

public class Computer {
    private Body body;
    private Keyboard keyboard;
    private Monitor monitor;
    private Speaker speaker;

    public void setBody(final Body body) {
        this.body = body;
    }

    public void setKeyboard(final Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void setMonitor(final Monitor monitor) {
        this.monitor = monitor;
    }

    public void setSpeaker(final Speaker speaker) {
        this.speaker = speaker;
    }

    public int getPrice() {
        return body.getPrice() + keyboard.getPrice() + monitor.getPrice() + speaker.getPrice();
    }

    public int getPower() {
        return body.getPower() + keyboard.getPower() + monitor.getPower() + speaker.getPower();
    }
}
