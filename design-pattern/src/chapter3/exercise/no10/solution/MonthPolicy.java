package chapter3.exercise.no10.solution;

public class MonthPolicy implements PayPolicy {

    @Override
    public int calculate(int workHours, int overtimeHours) {
        return 10000 * workHours +  11000 * overtimeHours;
    }
}
