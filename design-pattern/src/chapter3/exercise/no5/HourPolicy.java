package chapter3.exercise.no5;

public class HourPolicy implements PayPolicy{

    @Override
    public int calculate(final Employee employee) {
        return 10000 * (employee.getWorkHours() + employee.getOverTimeHours());
    }
}
