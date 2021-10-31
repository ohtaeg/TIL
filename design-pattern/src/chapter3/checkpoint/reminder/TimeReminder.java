package chapter3.checkpoint.reminder;

public class TimeReminder {
    private ReminderStrategy reminderStrategy;
    private MP3 mp3;

    public TimeReminder(final ReminderStrategy reminderStrategy) {
        this.reminderStrategy = reminderStrategy;
    }

    public void reminder() {
        mp3 = new MP3();
        if (reminderStrategy.isNow()) {
            mp3.playSong();
        }
    }

}
