package chapter7_state.exercise.vending_machine.beverage;

public interface SaleState {

    boolean isSoldOut();

    SaleState subtractStock();

    SaleState refill(final int stock);
}
