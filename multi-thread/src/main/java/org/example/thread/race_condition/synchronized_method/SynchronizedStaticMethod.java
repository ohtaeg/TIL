package org.example.thread.race_condition.synchronized_method;

public class SynchronizedStaticMethod {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Thread thread1 = new Thread(() -> counter.increment("thread1"));
        Thread thread2 = new Thread(() -> counter.decrement("thread2"));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("We currently have " + counter.getItems() + " items");
    }


    private static class Counter {
        private static int items = 0;

        public static synchronized void increment(String name) {
            System.out.println(name + " increment lock");
            for (int i = 0; i < 100_000_000; i++) {
                items++;
            }
            System.out.println(name + " increment unlock");
        }

        public synchronized void decrement(String name) {
            System.out.println(name + " decrement lock");
            for (int i = 0; i < 100_000_000; i++) {
                items--;
            }
            System.out.println(name + " decrement unlock");
        }

        public static synchronized int getItems() {
                return items;
        }
    }
}
