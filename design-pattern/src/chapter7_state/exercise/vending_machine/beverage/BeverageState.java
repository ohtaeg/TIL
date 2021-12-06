package chapter7_state.exercise.vending_machine.beverage;

public interface BeverageState {

    boolean isSoldOut();

    BeverageState subtractStock();

    BeverageState refill(final int stock);
}
