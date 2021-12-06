package chapter7_state.exercise.no5.beverage;

public class SoldOut implements BeverageState {

    @Override
    public boolean isSoldOut() {
        return true;
    }

    @Override
    public BeverageState subtractStock() {
        throw new UnsupportedOperationException("이미 품절된 음료수에요");
    }

    @Override
    public BeverageState refill(final int stock) {
        if (stock > 0) {
            return new Selling(stock);
        }
        return this;
    }
}
