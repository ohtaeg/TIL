package chapter9_observer.lecture.exercise.no1.after;

public class LowBatteryWarning implements Observer {

    private static final int LOW_BATTERY = 30;

    public void update(final Battery battery) {
        final int level = battery.getLevel();
        if (level < LOW_BATTERY) {
            System.out.println("[Warning] : Low Battery : " + level + " Compared with " + LOW_BATTERY);
        }
    }
}
