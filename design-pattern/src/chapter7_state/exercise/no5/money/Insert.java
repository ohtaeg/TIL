package chapter7_state.exercise.no5.money;

public class Insert implements MoneyState {

    private Money money;

    public Insert(final int money) {
        this.money = new Money(money);
    }

    @Override
    public MoneyState insert(final int money) {
        if (money < 0) {
            throw new RuntimeException("금액이 마이너스 일 수가 없어요");
        }

        this.money.add(money);
        System.out.println(this.money.getMoney());
        return this;
    }

    @Override
    public int refund() {
        return this.getMoney();
    }

    @Override
    public int getMoney() {
        return money.getMoney();
    }

    @Override
    public boolean isNotEnough() {
        return false;
    }

    @Override
    public void subtractMoney(final int money) {
        this.money.subtract(money);
    }
}
