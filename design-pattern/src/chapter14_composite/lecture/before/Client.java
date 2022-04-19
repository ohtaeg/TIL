package chapter14_composite.lecture.before;

public class Client {

    public static void main(String[] args) {
        Item doranBlade = new Item("도란검", 450);
        Item healPotion = new Item("체력 포션", 50);

        Bag bag = new Bag();
        bag.add(doranBlade);
        bag.add(healPotion);

        // 문제, 가방에 들어있는 아이템을 출력하는 것과 아이템을 출력하는 것에 대해
        Client client = new Client();
        client.printPrice(doranBlade);
        client.printPrice(bag);
    }

    private void printPrice(final Item item) {
        System.out.println(item.getPrice());
    }
    private void printPrice(final Bag bag) {
        System.out.println(bag.getItems().stream().mapToInt(Item::getPrice).sum());
    }
}
