package chapter3.checkpoint.reminder;

import java.time.LocalDateTime;

public class TimeReminderStrategy implements ReminderStrategy{

    private int hour;

    public TimeReminderStrategy() {
        this(10);
    }

    public TimeReminderStrategy(final int hour) {
        this.hour = hour;
    }

    @Override
    public boolean isNow() {
        return LocalDateTime.now().getHour() == hour;
    }
}
