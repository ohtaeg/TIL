package chapter7_state.exercise.vending_machine.beverage;

public class SoldOut implements SaleState {

    @Override
    public boolean isSoldOut() {
        return true;
    }

    @Override
    public SaleState subtractStock() {
        throw new UnsupportedOperationException("이미 품절된 음료수에요");
    }

    @Override
    public SaleState refill(final int stock) {
        if (stock > 0) {
            return new Selling(stock);
        }
        return this;
    }
}
