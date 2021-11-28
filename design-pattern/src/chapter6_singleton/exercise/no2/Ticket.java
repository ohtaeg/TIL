package chapter6_singleton.exercise.no2;

public class Ticket {

    private int serial;

    public Ticket(final int serial) {
        if (serial <= 0) {
            throw new IllegalArgumentException("유효하지 않은 시리얼");
        }
        this.serial = serial;
    }
}
