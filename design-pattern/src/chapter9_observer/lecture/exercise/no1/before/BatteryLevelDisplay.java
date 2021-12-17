package chapter9_observer.lecture.exercise.no1.before;

public class BatteryLevelDisplay {
    private Battery battery;

    public BatteryLevelDisplay(final Battery battery) {
        this.battery = battery;
    }

    public void update() {
        int level = battery.getLevel();
        System.out.println("Level : " + level);
    }

}
