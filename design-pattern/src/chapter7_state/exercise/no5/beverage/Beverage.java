package chapter7_state.exercise.no5.beverage;

public class Beverage {

    private int price;
    private String name;
    private int stock;

    public Beverage(final int price, final String name, final int stock) {
        this.price = price;
        this.name = name;
        this.stock = stock;
    }

    public boolean isSoldOut() {
        return stock <= 0;
    }

    public void subtractStock() {
        this.stock -= 1;
    }

    public boolean canSell(final int money) {
        return price <= money;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public void refill(final int stock) {
        this.stock += stock;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Beverage{" +
            "price=" + price +
            ", name='" + name + '\'' +
            ", stock=" + stock +
            '}';
    }
}
