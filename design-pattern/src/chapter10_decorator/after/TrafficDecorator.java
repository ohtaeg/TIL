package chapter10_decorator.after;

public class TrafficDecorator extends DisplayDecorator{

    public TrafficDecorator(final Display decoratorDisplay) {
        super(decoratorDisplay);
    }

    @Override
    public void draw() {
        super.draw();
        drawTraffic();
    }

    private void drawTraffic() {
        System.out.println("교통량 표시");
    }
}
