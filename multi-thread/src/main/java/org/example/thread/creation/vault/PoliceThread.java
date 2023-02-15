package org.example.thread.creation.vault;

public class PoliceThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(i);
        }

        System.out.println("Game over");
        System.exit(0);
    }
}
