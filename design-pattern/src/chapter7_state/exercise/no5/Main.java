package chapter7_state.exercise.no5;

import chapter7_state.exercise.no5.beverage.Beverage;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<Integer, Beverage> beverages = makeBeverages();
        VendingMachine vendingMachine = new VendingMachine(beverages);
    }

    private static Map<Integer, Beverage> makeBeverages() {
        Map<Integer, Beverage> beverages = new HashMap<>();
        beverages.put(1, new Beverage(1000, "콜라", 1));
        beverages.put(2, new Beverage(1100, "제로 콜라", 1));
        beverages.put(3, new Beverage(1200, "사이다", 1));
        return beverages;
    }
}
