package rx.toby.operator;

import java.util.concurrent.Flow;

public abstract class DelegateSubscriber<T, R> implements Flow.Subscriber<T> {

    private final Flow.Subscriber subscriber;

    public DelegateSubscriber(final Flow.Subscriber<? super R> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onSubscribe(final Flow.Subscription subscription) {
        subscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(final T item) {
        subscriber.onNext(item);
    }

    @Override
    public void onError(final Throwable throwable) {
        subscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        subscriber.onComplete();
    }
}
