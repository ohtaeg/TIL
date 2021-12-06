package chapter7_state.exercise.no5.beverage;

public class Beverage {

    private int price;
    private String name;
    private BeverageState beverageState;

    public Beverage(final int price, final String name, final int stock) {
        this.price = price;
        this.name = name;
        this.beverageState = findState(stock);
    }

    private BeverageState findState(final int stock) {
        if (stock > 0) {
            return new Selling(stock);
        }
        return new SoldOut();
    }

    public boolean isSoldOut() {
        return beverageState.isSoldOut();
    }

    public void subtractStock() {
        this.beverageState = this.beverageState.subtractStock();
    }

    public void refill(final int stock) {
        this.beverageState = this.beverageState.refill(stock);
    }

    public boolean canSell(final int money) {
        return price <= money;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Beverage{" +
            "price=" + price +
            ", name='" + name + '\'' +
            '}';
    }
}
