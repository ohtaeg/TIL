# 스레드 풀
## Thread per request model
- Requese 마다 Thread 하나를 할당
- 만약 해당 모델에서 요청마다 스레드를 새로 만들고, 처리가 끝나면 스레드를 버리는 식으로 동작한다면 어떤 문제점이 있을까?
  - 단순한 연산에 비해 커널 레벨에서 스레드를 생성하는데 소요되는 시간은 길기 때문에 스레드 생성에 소요되는 시간으로 인해 요청 처리가 더 오래 걸림
  - 스레드 생성 or 서버의 처리 속도보다 더 빠르게 요청이 늘어나면
    - 스레드가 계속 생성하여 스레드 수가 증가됨으로써 컨텍스트 스위칭의 빈번한 발생
    - CPU 오버헤드 증가로 CPU time 낭비
    - 어느 순간 서버 전체가 응답 불가능 상태에 빠진다.
  - 스레드가 계속 생성하여 스레드 수가 증가됨으로써 메모리가 점점 고갈된다.
- 리눅스에서는 스레드 수가 많아질수록 time slice는 줄어들게 되고, 스레드 수가 많아질수록 context switching이 더 자주 발생하게 된다.

<br>

## Thread pool
- 미리 스레드를 여러개 만들어 놓고 재사용
  - 스레드 생성 시간을 절약
- 제한된 갯수의 스레드를 운용
  - 스레드가 무제한으로 생성되는 것을 방지
- 스타크래프트의 캐리어와 인터셉터와 비슷해보임
  - 최대 8개의 인터셉터를 가질 수 있고, 미리 생성해둔다면 계속해서 공격 가능

### Thread pool 사용 사례
- 여러 작업을 동시에 처리해야할 때
- thread per request model
- task를 subtask로 나뉘어서 동시에 처리
- 순서 상관없이 동시 실행이 가능한 task 처리

### 스레드 풀에 몇 개의 스레드를 만들어 두는게 적절한가?
- CPU의 코어 갯수와 task의 성향에 따라 다름
- 만약 CPU bound task 라면, 코어 갯수 만큼 혹은 그 보다 몇 개 더 많은 정도
- 만약 I/O bound task 라면, 코어 갯수보다 1.5배? 그이상으로 할지 등 경험적인 결정이 필요

### 스레드 풀에 실행될 task 갯수의 제한이 없다면?
- 스레드 풀의 큐 사이즈가 제한이 있는지 확인 필요
- 요청은 큐에 쌓이게 되는데, 스레드 풀 내부에 처리할 스레드가 없다면 요청이 계속 큐에 쌓여 메모리 낭비가 발생

<br>

### Executors 클래스
- static 메서드로 다양한 형태의 스레드 풀을 제공하는 클래스
```
// 스레드 10개로 고정된 스레드 풀 생성
ExecutorService threadPool = Executors.newFixedThreadPool(10);
threadPool.submit(task1);
threadPool.submit(task2);
```
- Executors.newFixedThreadPool 내부 코드
```java
public class Executors {
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()); // 스레드 풀에 사용될 큐
    }
}
```
- LinkedBlockingQueue 생성 코드
```java
public class LinkedBlockingQueue<E> { 
    public LinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    }
    
    // capacity -> queue size, 21억
    public LinkedBlockingQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        last = head = new Node<E>(null);
    }
}
```
- 스레드 풀의 큐 사이즈는 21억을 기본으로 생성되기 때문에 스레드 풀 내부에 처리할 스레드가 없는데 요청이 계속 큐에 쌓여 메모리 낭비가 발생할 수 있는 것을 인지해야함
