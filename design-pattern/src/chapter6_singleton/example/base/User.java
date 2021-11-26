package chapter6_singleton.example.base;

public class User {
    private final String name;

    public User(final String name) {
        this.name = name;
    }

    public void print() {
        Printer printer = Printer.getPrinter();
        printer.print(this.name + ", print using : " + printer.toString() + ".");
    }
}
