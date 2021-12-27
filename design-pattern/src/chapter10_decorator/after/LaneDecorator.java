package chapter10_decorator.after;

public class LaneDecorator extends DisplayDecorator{

    public LaneDecorator(final Display decoratorDisplay) {
        super(decoratorDisplay);
    }

    @Override
    public void draw() {
        super.draw();
        drawLane();
    }

    private void drawLane() {
        System.out.println("차선 표시");
    }
}
