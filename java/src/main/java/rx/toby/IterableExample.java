package rx.toby;

import java.util.Iterator;

public class IterableExample {

    public static void main(String[] args) {
        Iterable<Integer> iterable = () -> new Iterator<>() {
            int i = 0;
            final static int MAX = 10;

            @Override
            public boolean hasNext() {
                return i < MAX;
            }

            @Override
            public Integer next() {
                return ++i;
            }
        };

        for (int i : iterable) {
            System.out.println(i);
        }
    }
}
