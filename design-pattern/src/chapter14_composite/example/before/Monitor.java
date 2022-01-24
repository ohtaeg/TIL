package chapter14_composite.example.before;

public class Monitor {

    private int price;
    private int power;

    public Monitor(final int price, final int power) {
        this.price = price;
        this.power = power;
    }

    public int getPrice() {
        return price;
    }

    public int getPower() {
        return power;
    }
}
