package chapter6_singleton.example.racing;

public class Printer {
    private static Printer printer = null;
    private int count = 0;

    private Printer() {
    }

    public static Printer getPrinter() {
        if (printer == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
            printer = new Printer();
        }
        return printer;
    }

    public void print(String text) {
        count++;
        System.out.println(text + ", count : " + count);
    }
}
