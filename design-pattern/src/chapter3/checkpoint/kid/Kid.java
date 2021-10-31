package chapter3.checkpoint.kid;

public class Kid {
    private Toy toy;

    public Kid(final Toy toy) {
        this.toy = toy;
    }

    public void play() {
        System.out.println(toy.toString());
    }
}
