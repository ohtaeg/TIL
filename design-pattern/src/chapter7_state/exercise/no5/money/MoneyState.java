package chapter7_state.exercise.no5.money;

public interface MoneyState {

    MoneyState insert(final int money);

    int refund();

    void subtractMoney(final int money);

    boolean isNotEnough();

    int getMoney();
}
