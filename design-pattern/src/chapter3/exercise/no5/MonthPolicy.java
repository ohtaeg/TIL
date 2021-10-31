package chapter3.exercise.no5;

public class MonthPolicy implements PayPolicy {

    @Override
    public int calculate(final Employee employee) {
        return 10000 * employee.getWorkHours() +  11000 * employee.getOverTimeHours();
    }
}
