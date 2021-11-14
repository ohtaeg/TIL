package chapter5_strategy.after;

import chapter5_strategy.after.strategy.attack.MissileStrategy;
import chapter5_strategy.after.strategy.attack.PunchStrategy;
import chapter5_strategy.after.strategy.moving.FlyingStrategy;
import chapter5_strategy.after.strategy.moving.WalkingStrategy;

public class Client {

    public static void main(String[] args) {
        Robot taekwonV = new TaekwonV("태권v");
        Robot atom = new Atom("아톰");

        System.out.println("My name is " + taekwonV.getName());
        taekwonV.setAttackStrategy(new MissileStrategy());
        taekwonV.setMovingStrategy(new WalkingStrategy());

        taekwonV.attack();
        taekwonV.move();

        System.out.println();

        System.out.println("My name is " + atom.getName());
        atom.setMovingStrategy(new FlyingStrategy());
        atom.setAttackStrategy(new PunchStrategy());

        atom.attack();
        atom.move();
    }
}
