package chapter5_strategy.before;

public class TaekwonV extends Robot {
    public TaekwonV(String name) {
        super(name);
    }

    public void attack() {
        System.out.println("I have Missile and can attack with it.");
    }

    public void move() {
        System.out.println("I can only walk");
    }
}
