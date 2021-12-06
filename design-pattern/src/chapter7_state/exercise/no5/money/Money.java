package chapter7_state.exercise.no5.money;

public class Money {
    private int money;

    public Money(final int money) {
        this.money = money;
    }

    public void add(final int money) {
        this.money += money;
    }

    public void subtract(final int money) {
        if (this.money - money < 0) {
            throw new RuntimeException("잔액이 마이너스일 수 없어요.");
        }

        this.money -= money;
    }

    public int getMoney() {
        return money;
    }
}
