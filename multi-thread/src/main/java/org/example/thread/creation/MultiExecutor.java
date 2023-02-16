package org.example.thread.creation;

import java.util.List;

/**
 * Thread 상속 대신 Runnable을 이용해서 생성하기
 */
public class MultiExecutor {

    private List<Runnable> tasks;

    public MultiExecutor(List<Runnable> tasks) {
        this.tasks = tasks;
    }

    public void executeAll() {
        this.tasks.forEach(Runnable::run);
    }
}
