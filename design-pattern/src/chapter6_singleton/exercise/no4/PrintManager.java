package chapter6_singleton.exercise.no4;

import java.util.ArrayList;
import java.util.List;

public class PrintManager {
    private static PrintManager INSTANCE = null;
    private static int DEFAULT_LIMIT_COUNT = 3;
    private static int LIMIT_COUNT = DEFAULT_LIMIT_COUNT;

    private List<Printer> managingPrinters = new ArrayList<>();

    private PrintManager(final int limit) {
        while (LIMIT_COUNT-- > 0) {
            managingPrinters.add(new Printer());
        }
    }

    public void setLimit(final int limit) {
        this.LIMIT_COUNT = limit;
    }

    public synchronized static PrintManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PrintManager(LIMIT_COUNT);
        }
        return INSTANCE;
    }

    public synchronized Printer getPrinter() {

        for (Printer printer : managingPrinters) {
            if (printer.isAvailable()) {
                printer.setAvailable(false);
                return printer;
            }
        }

        return managingPrinters.stream().findAny().get();
    }
}
