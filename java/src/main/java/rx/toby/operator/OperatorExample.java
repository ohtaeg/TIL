package rx.toby.operator;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.Function;
import java.util.stream.Stream;

public class OperatorExample {

    public static void main(String[] args) {
        List<Integer> numbers = Stream.iterate(1, a -> a + 1)
                .limit(10)
                .toList();

        Flow.Publisher<Integer> publisher = getIterablePublisher(numbers);
        Flow.Publisher<Integer> multiplyOperator = convert(publisher, a -> a * 10); // 10, 11, 12 ...
        Flow.Publisher<Integer> negativeOperator = convert(multiplyOperator, a -> -a); // -10, -11, 12 ...
        Flow.Publisher<Integer> sumOperator = sum(negativeOperator); // -550
        Flow.Publisher<String> printOperator = convert(sumOperator, a -> "[" + a + "]"); // [-550]

        Flow.Subscriber loggingSubscriber = getLoggingSubscriber();

        printOperator.subscribe(loggingSubscriber);
    }

    private static Flow.Publisher<Integer> getIterablePublisher(final Iterable<Integer> numbers) {
        return subscriber -> subscriber.onSubscribe(new Flow.Subscription() {
            @Override
            public void request(final long n) {
                try {
                    numbers.forEach(subscriber::onNext);
                    subscriber.onComplete();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }

            @Override
            public void cancel() {}
        });
    }

    private static <T, R> Flow.Publisher<R> convert(final Flow.Publisher<T> originPublisher, final Function<T, R> function) {
        return subscriber -> originPublisher.subscribe(new DelegateSubscriber<T, R>(subscriber) {
            @Override
            public void onNext(final T item) {
                subscriber.onNext(function.apply(item));
            }
        });
    }

    /**
     * sum operator (합계 계산 publisher)
     * 데이터 갯수와 상관없이 가공된 데이터를 최종으로 합계를 구하는 operator
     */
    private static Flow.Publisher<Integer> sum(final Flow.Publisher<Integer> publisher) {
        return subscriber -> publisher.subscribe(new DelegateSubscriber<Integer, Integer>(subscriber) {
            int sum = 0;

            @Override
            public void onNext(final Integer item) {
                sum += item;
            }

            @Override
            public void onComplete() {
                subscriber.onNext(sum);
                subscriber.onComplete();
            }
        });
    }

    private static <T> Flow.Subscriber<T> getLoggingSubscriber() {
        return new Flow.Subscriber<T>() {
            @Override
            public void onSubscribe(final Flow.Subscription subscription) {
                System.out.println("onSubscribe");
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(final T item) {
                System.out.println("onNext: " + item);
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
    }
}
