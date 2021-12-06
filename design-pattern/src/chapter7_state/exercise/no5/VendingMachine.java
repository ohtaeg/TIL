package chapter7_state.exercise.no5;

import chapter7_state.exercise.no5.beverage.Beverage;
import chapter7_state.exercise.no5.money.MoneyState;
import chapter7_state.exercise.no5.money.Waiting;
import java.util.Map;

public class VendingMachine {
    private MoneyState moneyState;
    private Map<Integer, Beverage> beverages;

    public VendingMachine(final Map<Integer, Beverage> beverages) {
        this.moneyState = Waiting.getInstance();
        this.beverages = beverages;
    }

    public void insert(int money) {
        this.moneyState = moneyState.insert(money);
    }

    public int refund() {
        final int remain = this.moneyState.refund();
        this.moneyState = Waiting.getInstance();
        System.out.println("옛다, " + remain);
        return remain;
    }

    public Beverage push(int buttonIndex) {
        if (moneyState.isNotEnough()) {
            throw new RuntimeException("넣은 돈이 없어요");
        }

        final Beverage beverage = beverages.get(buttonIndex);
        if (beverage == null || beverage.isSoldOut()) {
            throw new RuntimeException("재고가 읍서요");
        }

        if (beverage.canSell(moneyState.getMoney())) {
            beverage.subtractStock();
            moneyState.subtractMoney(beverage.getPrice());
            System.out.println(beverage);
            return beverage;
        }

       throw new RuntimeException("투입된 돈이 음료수 값보다 적어요");
    }
}
