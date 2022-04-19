package chapter9_observer.lecture.injava;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

public class SynchronizedFlowInJava {

    public static void main(String[] args) throws InterruptedException {
        Flow.Publisher<String> publisher = subscriber -> {
            subscriber.onNext("hello flow");
            subscriber.onComplete();
        };

        Flow.Subscriber<String> subscriber = new Flow.Subscriber<String>() {

            @Override
            public void onSubscribe(final Subscription subscription) {

            }

            @Override
            public void onNext(final String item) {
                System.out.println(item);

            }

            @Override
            public void onError(final Throwable throwable) {

            }

            @Override
            public void onComplete() {
                System.out.println("completed");
            }
        };

        publisher.subscribe(subscriber);
        System.out.println("현재 동기기 때문에 메시지를 받아서 처리할 때 까지 이 문장은 실행되지 않음. 끝나면 실행될 것임");
    }
}
