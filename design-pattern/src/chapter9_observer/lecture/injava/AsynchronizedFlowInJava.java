package chapter9_observer.lecture.injava;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

public class AsynchronizedFlowInJava {

    public static void main(String[] args) throws InterruptedException {
        // Publisher 중에 SubmissionPublisher 구현체가 있는데, 비동기 flow 기능을 제공한다.
        Flow.Publisher<String> publisher = new SubmissionPublisher<>();
        Flow.Subscriber<String> subscriber = new Flow.Subscriber<String>() {

            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(final Subscription subscription) {
                System.out.println("sub!");
                this.subscription = subscription;
                this.subscription.request(1); // 퍼블리셔한테 하나를 꺼내옴 꺼내오면 알아서 onNext를 자동으로 호출해준다.
            }

            @Override
            public void onNext(final String item) {
                System.out.println("onNext Called");
                System.out.println(Thread.currentThread().getName());
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
        ((SubmissionPublisher) publisher).submit("hello java");

        System.out.println("이게 먼저 출력될 수 있슴");
    }
}
