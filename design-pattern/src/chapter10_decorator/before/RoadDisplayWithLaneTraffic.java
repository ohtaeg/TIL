package chapter10_decorator.before;

// 차선과 교통랑 표시
public class RoadDisplayWithLaneTraffic extends RoadDisplay {

    @Override
    void draw() {
        super.draw();
        drawLane();
        drawTraffic();
    }

    private void drawLane() {
        System.out.println("차선 표시");
    }

    private void drawTraffic() {
        System.out.println("교통랑 표시");
    }
}
