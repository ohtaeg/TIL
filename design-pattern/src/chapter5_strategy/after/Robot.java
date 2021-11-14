package chapter5_strategy.after;

import chapter5_strategy.after.strategy.attack.AttackStrategy;
import chapter5_strategy.after.strategy.moving.MovingStrategy;

public abstract class Robot {
    private String name;
    private MovingStrategy movingStrategy;
    private AttackStrategy attackStrategy;

    public Robot(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMovingStrategy(final MovingStrategy movingStrategy) {
        this.movingStrategy = movingStrategy;
    }

    public void setAttackStrategy(final AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    public void attack() {
        attackStrategy.attack();
    }
    public void move() {
        movingStrategy.move();
    }
}
