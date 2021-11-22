package chapter5_strategy.exercise.no4.move;

import chapter5_strategy.exercise.no4.Ball;
import chapter5_strategy.exercise.no4.BallFrame;

public class VerticalStrategy extends DirectionStrategy {

    public void move(Ball ball) {
        ball.setIntervals(0, Ball.INTERVAL);

        while (true) {
            ball.setY(ball.getY() + ball.getYinterval());

            if (isNeverThenMoveBottom(ball.getY()) || isOverHeight(ball.getY())) {
                ball.setIntervals(0, ball.getYinterval() * -1);
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }
    }


    private boolean isOverHeight(final int y) {
        return y + Ball.SIZE >= BallFrame.HEIGHT;
    }

    private boolean isNeverThenMoveBottom(final int position) {
        return position <= 0;
    }
}