package chapter5_strategy.after.strategy.moving;

public class FlyingStrategy implements MovingStrategy {
    @Override
    public void move() {
        System.out.println("I can fly");
    }
}
