package chapter7_state.exercise.no5.beverage;

public class Selling implements BeverageState {

    private int stock;

    public Selling(final int stock) {
        this.stock = stock;
    }

    @Override
    public boolean isSoldOut() {
        return false;
    }

    @Override
    public BeverageState subtractStock() {
        this.stock -= 1;
        if (stock == 0) {
            return new SoldOut();
        }
        return this;
    }

    @Override
    public BeverageState refill(final int stock) {
        this.stock += stock;
        return this;
    }
}
