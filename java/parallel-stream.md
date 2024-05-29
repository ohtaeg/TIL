# Parallel Stream
- 병렬 처리를 쉽게 해줄 수 있는 Stream

## 동작 원리
- 병렬 스트림에서는 기본적으로 `ForkJoinFramework` 이라는 방식을 사용한다.
- Thread pool 크기를 조절하는 방법은 2가지가 있다.

1. System.setProperty()를 통해 설정
    > System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");
    
    > -D java.util.concurrent.ForkJoinPool.common.parallelism=12

2. ForkJoinPool 사용
    
## ForkJoinFramework
- 기본적으로 큰 업무를 작은 업무로 나누어 배분해서 (fork) 쪼개진 작업들을 work thread가 처리하고 합치는(join) 방식
- 분할 정복 알고리즘과 흡사
  1. 큰 업무를 작은 업무로 쪼갠다.
  2. 부모 쓰레드로부터 처리 로직을 복사하여 새로운 쓰레드에 쪼개진 업무를 수행시킨다.
  3. 더이상 Fork가 일어나지 않고 업무가 완료되면 부모 쓰레드에 Join하여 취합한다.
  4. 최초 ForkJoinPool을 생성한 쓰레드로 값을 리턴하여 작업을 완료

<br>

-  **ForkJoinPool은 ExecutorService의 구현체**
   - 일반적으로 ForkJoinFramework == ForkJoinPool 이라고 편의상 칭하는듯하다.
   - 일반 ExecutorService 구현 클래스와는 다른점이 하나 존재하는데 `work-stealing 알고리즘`이 구현되어있다

## Work-Stealing
여러 쓰레드에 작업을 분배할때 처음부터 각 쓰레드에 작업을 공평하게 나눌 수 없기 때문에(작업 시간의 차이)

대기중인 쓰레드가 바쁜 쓰레드의 작업을 가져오도록 하여 서로 도와가며 빠른 시간안에 처리하는 알고리즘

ForkJoinPool 내부에 쓰레드 개별 dequeue를 가지고 있고 다른 쓰레드에서 작업을 가져올 떄에는 반대쪽 방향에서 꺼내온다.


<br>
<br>

## Parallel Stream 사용시 조심해야할 점
### 쓰레드 풀 공유
별도의 설정이 없다면 기본적으로 Parallel Stream별로 스레드 풀을 만드는 것이 아닌 하나의 스레드 풀을 모든 Parallel Stream이 공유한다.

즉, ForkJoinPool을 사용하는 다른 스레드에 영향을 줄 수 있으며, 반대로 다른 스레드에 의해 영향을 받을 수 있다.
```
IntStream.range(1,10).parallel()
         .peek( i -> { System.out.println(Thread.currentThread().getName()); })
         .sum();
         
ForkJoinPool.commonPool-worker-3
ForkJoinPool.commonPool-worker-4
ForkJoinPool.commonPool-worker-9
ForkJoinPool.commonPool-worker-1
ForkJoinPool.commonPool-worker-2
```

각 Parallel Stream별로 ForkJoinPool을 생성하여 독립적인 스레드풀을 가지게 할 순 있지만 오라클이 권장하지 않음.

```
ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
forkJoinPool.submit(() -> {   
    numbers.parallelStream().forEach((number) -> { ... })
}
```

### Collection 별 성능 차이 존재
Parallel Stream은 작업을 분할하기 위해 Spliterator를 이용하는데

사이즈나 인덱스로 접근하는 array, arrayList 같은 컬렉션들은 괜찮지만

HashSet 같은 컬렉션들은 효과적이지 않다.

```java
 public ArrayListSpliterator trySplit() {
    int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
    return (lo >= mid) 
            ? null 
            : // divide range in half unless too small
            new ArrayListSpliterator(lo, index = mid, expectedModCount);
}
```

### 기존 ExecutorService의 방식보다 느릴수도 있다. 순차 스트림보다 느릴 수 있다.
- 분할하면서 테스크 객체가 만들어지는 비용
- 작업들을 분할(fork)하고 다시 합치는(join) 비용
- 스레드 간의 컨텍스트 스위칭 비용

와 같은 오버헤드가 존재하여 사용해야되는 상황이라면 측정 및 고려를 해야한다.


<br>
<br>

## Parallel Stream 언제 사용해야할까?
javadoc에 따르면 

> The efficiency of ForkJoinTasks stems from a set of restrictions (that are only partially statically enforceable) reflecting their main use as computational tasks calculating pure functions or operating on purely isolated objects.

주로 순수 함수 계산이나 완전히 격리된 객체를 다루는 계산 작업

> Computations should ideally avoid synchronized methods or blocks, and should minimize other blocking synchronization

계산 작업은 가능한 한 synchronized 메서드나 블록을 피해야 하며, 다른 형태의 블로킹 동기화도 최소화해야 할 때

> Subdividable tasks should also not perform blocking I/O

또한 블로킹 I/O를 수행하지 않을 때



그 외에도 가이드 라인이 존재한다.

https://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html