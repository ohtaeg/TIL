package org.example.thread.creation;

public class ThreadCreation {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("hi i'm new thraead, " + Thread.currentThread().getName());
            System.out.println("Current thraead priority is  " + Thread.currentThread().getPriority());
            throw new RuntimeException("일부러 발생시킨 예외, 핸들러가 잡아주센");
        });

        // 쓰레드의 이름을 설정할 수 있다.
        thread.setName("newer");

        // OS가 스케줄링시 동적 우선순위를 적용할 수 있도록 조정 가능, 1 ~ 10까지 가능
        thread.setPriority(Thread.MAX_PRIORITY);

        thread.setUncaughtExceptionHandler((t, e) -> System.out.println("A critical error happened in thread, " + t.getName() + ", " + e.getMessage()));

        // 현재 쓰레드 객체의 이름을 얻어온다.
        System.out.println("current : " + Thread.currentThread().getName() + ", before start");
        thread.start();
        System.out.println("current : " + Thread.currentThread().getName() + ", after start");


        Thread otherThread = new NewThread();
        otherThread.start();
    }

    // 상속을 이용해서 생성할 수 있다. 이때는 자기 자신을 통해 정보를 가져올 수 있다.
    private static class NewThread extends Thread {
        @Override
        public void run() {
            System.out.println("hi i'm new other thraead, " + this.getName());
        }
    }
}