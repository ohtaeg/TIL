package chapter14_composite.example.after;


import java.util.ArrayList;
import java.util.List;

public class Computer extends ComputerDevice {

    private List<ComputerDevice> components = new ArrayList<>();

    public void addComponent(final ComputerDevice component) {
        this.components.add(component);
    }

    public void removeComponent(final ComputerDevice computerDevice) {
        this.components.remove(computerDevice);
    }

    public int getPrice() {
        return components.stream()
                         .reduce(0, (sum, component) -> sum + component.getPrice(), Integer::sum);
    }

    @Override
    public int getPower() {
        return components.stream()
                         .reduce(0, (sum, component) -> sum + component.getPower(), Integer::sum);
    }
}
