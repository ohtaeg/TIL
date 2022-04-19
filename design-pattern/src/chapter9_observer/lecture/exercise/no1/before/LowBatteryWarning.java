package chapter9_observer.lecture.exercise.no1.before;

public class LowBatteryWarning {
    private static final int LOW_BATTERY = 30;
    private Battery battery;

    public LowBatteryWarning(final Battery battery) {
        this.battery = battery;
    }

    public void update() {
        final int level = battery.getLevel();
        if (level < LOW_BATTERY) {
            System.out.println("[Warning] : Low Battery : " + level + " Compared with " + LOW_BATTERY);
        }
    }
}
