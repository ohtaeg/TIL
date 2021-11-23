package chapter5_strategy.exercise.no5.printer;

public class SimplePrinter implements Printer {

    @Override
    public void print(final String text) {
        System.out.println("simple");
        System.out.println(text);
    }
}
