package chapter10_decorator.after;

public abstract class DisplayDecorator extends Display {
    private Display decoratorDisplay;

    public DisplayDecorator(final Display decoratorDisplay) {
        this.decoratorDisplay = decoratorDisplay;
    }

    public void draw() {
        decoratorDisplay.draw();
    }
}
