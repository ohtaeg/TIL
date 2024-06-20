package rx.toby.operator;

import reactor.core.publisher.Flux;

/**
 * OperatorExample의 행위들을 Reactor 라이브러리를 이용하여 간단하게 할 수 있다.
 * Publisher 인터페이스와 Operator를 사용하도록 구현한 Flux 제공
 */
public class ReactorExample {
    public static void main(String[] args) {
        Flux.<Integer>create(s -> {
                    s.next(1);
                    s.next(2);
                    s.next(3);
                    s.complete();
                })
                .log()
                .map(s -> s * 10)
                .reduce(0, Integer::sum)
                .log()
                .subscribe(System.out::println);
    }
}
