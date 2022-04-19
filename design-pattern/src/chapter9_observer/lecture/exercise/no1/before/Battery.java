package chapter9_observer.lecture.exercise.no1.before;

public class Battery {
    private int level = 100;
    private BatteryLevelDisplay display;
    private LowBatteryWarning warning;

    public void setDisplay(final BatteryLevelDisplay display) {
        this.display = display;
    }

    public void setWarning(final LowBatteryWarning warning) {
        this.warning = warning;
    }

    public void consume(int amount) {
        level -= amount;
        display.update();
        warning.update();
    }

    public int getLevel() {
        return level;
    }
}
