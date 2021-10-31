package chapter3.exercise.no5;

public class Employee {
    private String id;
    private String name;
    private int workHours;
    private int overTimeHours;

    private PayPolicy payStrategy;

    public int calculatePay() {
        return payStrategy.calculate(this);
    }

    public int getWorkHours() {
        return workHours;
    }

    public int getOverTimeHours() {
        return overTimeHours;
    }
}
