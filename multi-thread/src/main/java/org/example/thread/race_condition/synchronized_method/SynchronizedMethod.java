package org.example.thread.race_condition.synchronized_method;

public class SynchronizedMethod {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Thread thread1 = new Thread(() -> counter.increment("thread1"));
        Thread thread2 = new Thread(() -> counter.decrement("thread2"));

        thread1.start();
        thread2.start();

        System.out.println("We currently have " + counter.getItems() + " items");
    }

    // 서로 다른 인스턴스이기에 스레드 1, 2가 increment(), decrement() 진입할 수 있다.
//    public static void main(String[] args) throws InterruptedException {
//        Counter counter = new Counter();
//        Counter counter2 = new Counter();
//
//        Thread thread1 = new Thread(() -> counter.increment("thread1"));
//        Thread thread2 = new Thread(() -> counter2.increment("thread2"));
//
//        thread1.start();
//        thread2.start();
//
//        System.out.println("We currently have " + counter.getItems() + " items");
//        System.out.println("We currently have " + counter2.getItems() + " items");
//    }

    private static class Counter {
        private int items = 0;

        public synchronized void increment(String name) {
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

        public synchronized int getItems() {
                return items;
        }
    }
}
