package chapter7_state.exercise.vending_machine.beverage;

public class Selling implements SaleState {

    private int stock;

    public Selling(final int stock) {
        this.stock = stock;
    }

    @Override
    public boolean isSoldOut() {
        return false;
    }

    @Override
    public SaleState subtractStock() {
        this.stock -= 1;
        if (stock == 0) {
            return new SoldOut();
        }
        return this;
    }

    @Override
    public SaleState refill(final int stock) {
        this.stock += stock;
        return this;
    }
}
