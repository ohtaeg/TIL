package chapter5_strategy.exercise.no4.move;

import chapter5_strategy.exercise.no4.Ball;
import chapter5_strategy.exercise.no4.BallFrame;

public class DiagonalMoveStrategy extends DirectionStrategy {

    public void move(Ball ball) {
        ball.setIntervals(1, 1);

        while (true) {
            ball.setX(ball.getX() + ball.getXinterval());
            ball.setY(ball.getY() + ball.getYinterval());

            if (isNeverThenMoveBottom(ball.getX())) {
                ball.setIntervals(ball.getXinterval() * -1, ball.getYinterval());
            }

            if (isOverWeight(ball.getX())) {
                ball.setIntervals(ball.getXinterval() * -1, ball.getYinterval());
            }

            if (isNeverThenMoveBottom(ball.getY())) {
                ball.setIntervals(ball.getXinterval(), ball.getYinterval() * -1);
            }

            if (isOverHeight(ball.getY())) {
                ball.setIntervals(ball.getXinterval(), -ball.getYinterval());
            }


            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }
    }

    private boolean isOverWeight(final int x) {
        return x + Ball.SIZE >= BallFrame.WIDTH;
    }

    private boolean isOverHeight(final int y) {
        return y + Ball.SIZE >= BallFrame.HEIGHT;
    }

    private boolean isNeverThenMoveBottom(final int position) {
        return position <= 0;
    }
}