package chapter5_strategy.exercise.no4.move;

import chapter5_strategy.exercise.no4.Ball;
import chapter5_strategy.exercise.no4.BallFrame;

public class HorizontalMoveStrategy extends DirectionStrategy {

    public void move(Ball ball) {
        ball.setIntervals(Ball.INTERVAL, 0);
        while (true) {
            ball.setX(ball.getX() + ball.getXinterval());
            if ((ball.getX() < 0 && ball.getXinterval() < 0)
                || (ball.getX() + Ball.SIZE > BallFrame.WIDTH - 15 && ball.getXinterval() > 0)) {
                ball.setIntervals(-ball.getXinterval(), 0);
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }
    }
}
