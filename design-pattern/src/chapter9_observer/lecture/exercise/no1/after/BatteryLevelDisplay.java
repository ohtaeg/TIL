package chapter9_observer.lecture.exercise.no1.after;

public class BatteryLevelDisplay implements Observer {

    @Override
    public void update(final Battery battery) {
        int level = battery.getLevel();
        System.out.println("Level : " + level);
    }
}
