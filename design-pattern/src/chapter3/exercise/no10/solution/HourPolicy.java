package chapter3.exercise.no10.solution;

public class HourPolicy implements PayPolicy {

    @Override
    public int calculate(int workHours, int overtimeHours) {
        return 10000 * (workHours + overtimeHours);
    }
}
