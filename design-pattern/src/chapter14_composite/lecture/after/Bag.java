package chapter14_composite.lecture.after;

import java.util.ArrayList;
import java.util.List;

public class Bag implements Component {
    private List<Component> items = new ArrayList<>();

    public void add(Component item) {
        this.items.add(item);
    }

    public List<Component> getItems() {
        return this.items;
    }

    @Override
    public int getPrice() {
        return items.stream().mapToInt(Component::getPrice).sum();
    }
}
