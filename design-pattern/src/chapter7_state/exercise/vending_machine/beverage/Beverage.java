package chapter7_state.exercise.vending_machine.beverage;

public class Beverage {

    private int price;
    private String name;
    private SaleState saleState;

    public Beverage(final int price, final String name, final int stock) {
        this.price = price;
        this.name = name;
        this.saleState = findState(stock);
    }

    private SaleState findState(final int stock) {
        if (stock > 0) {
            return new Selling(stock);
        }
        return new SoldOut();
    }

    public boolean isSoldOut() {
        return saleState.isSoldOut();
    }

    public void subtractStock() {
        this.saleState = this.saleState.subtractStock();
    }

    public void refill(final int stock) {
        this.saleState = this.saleState.refill(stock);
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
