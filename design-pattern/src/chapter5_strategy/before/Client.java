package chapter5_strategy.before;

public class Client {

    public static void main(String[] args) {
        Robot taekwonV = new TaekwonV("태권v");
        Robot atom = new Atom("아톰");

        System.out.println("My name is " + taekwonV.getName());
        taekwonV.move();
        taekwonV.attack();

        System.out.println();

        System.out.println("My name is " + atom.getName());
        atom.move();
        atom.attack();
    }
}
