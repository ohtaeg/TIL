package chapter14_composite.lecture.after;

public class Item implements Component {
    private String name;

    private int price;

    public Item(final String name, final int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public int getPrice() {
        return price;
    }
}
