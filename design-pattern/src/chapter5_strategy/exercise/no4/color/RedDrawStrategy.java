package chapter5_strategy.exercise.no4.color;

import chapter5_strategy.exercise.no4.Ball;
import java.awt.Color;

public class RedDrawStrategy extends DrawStrategy {

    @Override
    public void draw(Ball ball) {
        ball.setColor(Color.red);
    }

}