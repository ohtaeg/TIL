package chapter6_singleton.exercise.no2;

public class TicketMachine {
    private static TicketMachine INSTANCE = null;
    private int limit;
    private int count;

    public synchronized static TicketMachine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TicketMachine();
        }
        return INSTANCE;
    }

    public synchronized void setLimit(final int limit) {
        this.limit = limit;
    }

    public synchronized Ticket publish() {
        if (this.count < this.limit) {
            count++;
            return new Ticket(SerialGenerator.create());
        }

        throw new IllegalArgumentException("티켓 발행이 초과하였습니다.");
    }
}
