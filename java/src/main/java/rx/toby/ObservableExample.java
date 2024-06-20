package rx.toby;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("deprecation")
public class ObservableExample {

    private static class IntObservable extends Observable implements Runnable {
        @Override
        public void run() {
            for (int i=1; i <= 10; i++) {
                // 관심을 가진 타겟들에게 새로운 변화가 있다는 것을 알려준다.
                setChanged();
                // 전달
                notifyObservers(i);
            }
        }
    }

    public static void main(String[] args) {
        Observer observer = (o, arg) -> System.out.println(Thread.currentThread().getName() + " " + arg);

        IntObservable intObservable = new IntObservable();
        intObservable.addObserver(observer);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(intObservable);
        executorService.shutdown();
    }
}
