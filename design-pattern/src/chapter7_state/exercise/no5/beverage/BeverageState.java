package chapter7_state.exercise.no5.beverage;

public interface BeverageState {

    boolean isSoldOut();

    BeverageState subtractStock();

    BeverageState refill(final int stock);
}
