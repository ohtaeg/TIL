package chapter11_template.exercise;

public class Customer {
    private String name;
    private int point;

    public Customer(final String name, final int point) {
        this.name = name;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(final int point) {
        this.point = point;
    }
}
