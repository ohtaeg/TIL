package chapter9_observer.lecture.exercise.no1.after;

public class Client {

    public static void main(String[] args) {
        Battery battery = new Battery();

        Observer display = new BatteryLevelDisplay();
        Observer warning = new LowBatteryWarning();

        battery.attach(display);
        battery.attach(warning);

        battery.consume(20);
        battery.consume(50);
        battery.consume(10);
    }
}
