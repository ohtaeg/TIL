package chapter14_composite.example.after;

public class Body extends ComputerDevice {
    private int price;
    private int power;

    public Body(final int price, final int power) {
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
