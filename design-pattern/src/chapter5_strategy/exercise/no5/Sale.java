package chapter5_strategy.exercise.no5;

import chapter5_strategy.exercise.no5.printer.HD108ReceiptPrinter;
import chapter5_strategy.exercise.no5.printer.Printer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sale {

    private List<Item> items = new ArrayList<>();
    private Printer printer = new HD108ReceiptPrinter();

    public void printReceipt() {
        Iterator<Item> itr = items.iterator();
        StringBuffer buf = new StringBuffer();
        while (itr.hasNext()) {
            Item item = itr.next();
            buf.append(item.getName());
            buf.append(", ");
            buf.append(item.getPrice());
            buf.append(System.lineSeparator());
        }

        printer.print(buf.toString());
    }

    public void add(Item item) {
        items.add(item);
    }

    public void changePrinter(final Printer printer) {
        this.printer = printer;
    }
}