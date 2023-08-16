package org.example.thread.race_condition.synchronized_block;

public class SynchronizedStaticBlock {
    public static void main(String[] args) throws InterruptedException {
        Counter counter1 = new Counter();
        Counter counter2 = new Counter();

        Thread thread1 = new Thread(() -> counter1.increment("thread1"));
        Thread thread2 = new Thread(() -> counter2.decrement("thread2"));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("counter1 currently have " + counter1.getItems() + " items");
        System.out.println("counter2 currently have " + counter2.getItems() + " items");
    }

    private static class Counter {
        private static int items = 0;

        public static void increment(String name) {
            synchronized (Counter.class) {
                System.out.println(name + " increment lock");
                for (int i = 0; i < 100_000_000; i++) {
                    items++;
                }
                System.out.println(name + " increment unlock");
            }
        }

        public static void decrement(String name) {
            synchronized (Counter.class) {
                System.out.println(name + " decrement lock");
                for (int i = 0; i < 100_000_000; i++) {
                    items--;
                }
                System.out.println(name + " decrement unlock");
            }
        }

        public synchronized int getItems() {
                return items;
        }
    }
}
