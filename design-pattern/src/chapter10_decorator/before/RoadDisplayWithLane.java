package chapter10_decorator.before;

// 도로에 차선 표시
public class RoadDisplayWithLane extends RoadDisplay {

    @Override
    void draw() {
        super.draw();
        drawLane();
    }

    private void drawLane() {
        System.out.println("차선 표시");
    }
}
