package chapter5_strategy.exercise.no4;

import chapter5_strategy.exercise.no4.color.RedDrawStrategy;
import chapter5_strategy.exercise.no4.move.DiagonalMoveStrategy;
import chapter5_strategy.exercise.no4.move.VerticalStrategy;

public class Client {

    private static final int INIT_LOCATION[] = {50, 100, 150};

    public static void main(String[] args) {
        Ball balls[] = new Ball[3];
        for (int i = 0; i < balls.length; i++) {
            balls[i] = new Ball(INIT_LOCATION[i], INIT_LOCATION[i]);
            // balls[i].setDirectionStrategy(new HorizontalMoveStrategy());
            // balls[i].setDirectionStrategy(new VerticalStrategy());
            balls[i].setDirectionStrategy(new DiagonalMoveStrategy());
            balls[i].setDrawStrategy(new RedDrawStrategy());
            balls[i].start();
        }
        new BallFrame(balls);
    }
}
