package org.example.thread.interrupt.example;

/**
 * 잘못된 시간을 차단하는 작업을 수행
 * sleep 메서드를 호출하여 잠재운다.
 * 해당 쓰레드가 외부에서 인터럽트되면 예외 처
 */
public class BlockingTask implements Runnable {

    public static void main(String[] args) {
        Thread thread = new Thread(new BlockingTask());

        // BlockingTask 쓰레드를 실행하게 되면 모든 쓰레드가 종료되었음에도 해당 쓰레드가 종료가 되지 않아 어플리케이션이 종료가 되지 않는다.
        thread.start();

        // 블로킹 된 쓰레드를 종료하기 위해 interrupt 호출
        thread.interrupt();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100_000);
        } catch (InterruptedException e) {
            System.out.println("Blocking task end");
        }
    }
}
