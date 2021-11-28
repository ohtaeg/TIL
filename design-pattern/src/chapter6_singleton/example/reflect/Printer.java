package chapter6_singleton.example.reflect;

public class Printer {
    private volatile static Printer printer = null;
    private int count = 0;

    private Printer() {
        if (printer != null) {
            throw new ExceptionInInitializerError("생성자를 통해 접근할 수 없습니다.");
        }
    }

    public static Printer getPrinter() {
        if (printer == null) {
            synchronized (Printer.class) {
                if (printer == null) {
                    printer = new Printer();
                }
            }
        }
        return printer;
    }

    public synchronized void print() {
        count++;
    }
}