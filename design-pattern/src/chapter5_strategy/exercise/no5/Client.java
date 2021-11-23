package chapter5_strategy.exercise.no5;

public class Client {

    public static void main(String[] args) {
        Item item1 = new Item("HUM", 12_000);
        Item item2 = new Item("BIT_COIN", 1_000_000_000);
        Sale sale = new Sale();

        sale.add(item1);
        sale.add(item2);

        sale.printReceipt();
    }
}
