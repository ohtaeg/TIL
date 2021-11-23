package chapter5_strategy.exercise.no5.printer;

public class HD108ReceiptPrinter implements Printer {

    public void print(String text) {
        System.out.println("----------");
        System.out.println(text);
        System.out.println("----------");
    }
}
