package org.example.thread.race_condition.synchronized_block;

/**
 * Critical Section & Synchronization
 */
public class SynchronizedBlock {
    public static void main(String[] args) throws InterruptedException {

        Counter counter1 = new Counter();
        Counter counter2 = new Counter();

        Thread thread1 = new Thread(() -> {
            counter1.increment("thread1");
        });
        Thread thread2 = new Thread(() -> {
            counter2.decrement("thread2");
        });

        thread1.start();
        thread2.start();

        System.out.println("counter1 currently have " + counter1.getItems() + " items");
        System.out.println("counter2 currently have " + counter2.getItems() + " items");
    }

    private static class Counter {
        private int items = 0;

        public void increment(String name) {
            synchronized(this) {
                System.out.println(name + " increment lock");
                for (int i = 0; i < 100_000_000; i++) {
                    items++;
                }
                System.out.println(name + " increment unlock");
            }
        }

        public void decrement(String name) {
            synchronized(this) {
                System.out.println(name + " decrement lock");
                for (int i = 0; i < 100_000_000; i++) {
                    items--;
                }
                System.out.println(name + " decrement unlock");
            }
        }

        public int getItems() {
            synchronized(this) {
                return items;
            }
        }
    }
}
