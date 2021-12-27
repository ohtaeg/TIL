package chapter10_decorator.before;

// 교통량 표시
public class RoadDisplayWithTraffic extends RoadDisplay {

    @Override
    void draw() {
        super.draw();
        drawTraffic();
    }

    private void drawTraffic() {
        System.out.println("교통랑 표시");
    }
}
