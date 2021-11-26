package chapter6_singleton.example.base;

public class Printer {
    private static Printer printer = null;

    private Printer() {
    }

    public static Printer getPrinter() {
        if (printer == null) {
            printer = new Printer();
        }
        return printer;
    }

    public void print(String text) {
        System.out.println(text);
    }
}
