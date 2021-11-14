package chapter5_strategy.before;

public class Atom extends Robot {

    public Atom(final String name) {
        super(name);
    }

    @Override
    public void attack() {
        System.out.println("I have strong punch and can attack with it");
    }

    @Override
    public void move() {
        System.out.println("I can fly");
    }
}
