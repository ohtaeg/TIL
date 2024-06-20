package rx.toby;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Flow;

public class PubSubExample {
    public static void main(String[] args) {
        Flow.Publisher publisher = subscriber -> {

            Iterator<Integer> iterator = Arrays.asList(1, 2, 3, 4, 5).iterator();

            subscriber.onSubscribe(new Flow.Subscription() {
                @Override
                public void request(final long n) {
                    try {
                        for (int i = 0; i < n; i++) {
                            if (!iterator.hasNext()) {
                                subscriber.onComplete();
                                break;
                            }
                            subscriber.onNext(iterator.next());
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }

                @Override
                public void cancel() {
                    System.out.println("cancel");
                }
            });
        };

        Flow.Subscriber subscriber = new Flow.Subscriber() {

            Flow.Subscription subscription;
            @Override
            public void onSubscribe(final Flow.Subscription subscription) {
                System.out.println("onSubscribe");
                this.subscription = subscription;
                this.subscription.request(1);
                // subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(final Object item) {
                System.out.println("onNext: " + item);
                this.subscription.request(1);
            }

            @Override
            public void onError(final Throwable throwable) {
                System.out.println("onError: " + throwable);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        publisher.subscribe(subscriber);
    }
}
