package chapter7_state.exercise.vending_machine.money;

public interface MoneyState {

    MoneyState insert(final int money);

    int refund();

    void subtractMoney(final int money);

    boolean isNotEnough();

    int getMoney();
}
