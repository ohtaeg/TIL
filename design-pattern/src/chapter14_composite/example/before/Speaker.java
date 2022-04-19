package chapter14_composite.example.before;

public class Speaker {
    private int price;
    private int power;

    public Speaker(final int price, final int power) {
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
