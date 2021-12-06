package chapter7_state.exercise.no5.money;

public class Waiting implements MoneyState {

    private Waiting() {}

    @Override
    public MoneyState insert(final int money) {
        if (money < 0) {
            throw new RuntimeException("금액이 마이너스 일 수가 없어요");
        }
        System.out.println(money);
        return new Insert(money);
    }

    @Override
    public int refund() {
        throw new UnsupportedOperationException("환불할 수 없는 상태입니다. 돈이 없어요");
    }

    @Override
    public void subtractMoney(final int money) {
        throw new UnsupportedOperationException("환불할 수 없는 상태입니다. 돈이 없어요");
    }

    @Override
    public boolean isNotEnough() {
        return true;
    }

    @Override
    public int getMoney() {
        return 0;
    }

    public static MoneyState getInstance() {
        return LazyHolder.moneyState;
    }

    private static class LazyHolder {
        private static final MoneyState moneyState = new Waiting();
    }
}
