# 모니터 락
- mutex와 semaphore는 편리하고 효과적인 락이지만 사용하기가 어렵다.
- 게다가 타이밍 에러가 가끔 발생한다.
  - 바이너리 세마포어를 여러 프로세스가 사용할 때 만약 wait()를 하고 signal()을 호출해야하는데 해당 순서가 안지켜지게 되면 두개 이상의 프로세스가 크리티컬 섹션에 동시에 들어갈 수 있다. 
- 이로 인한 문제를 방지하기 위해 자바에서는 모니터 락을 제공한다.
- concurrency mechanism for thread synchronization
- **mutual exclusion을 보장**
- **조건에 따라 스레드가 대기 상태로 전환 기능**
- 자바에서 두 키워드를 사용하여 모니터 락을 사용할 수 있다.
  - synchronized keyword
  - wait() and notify()

### synchronized
- 모니터의 mutual exclusion 기능은 해당 키워드를 사용한다.
  - critical section에 해당하는 코드 블록을 명시적으로 선언할 때 사용하는 자바 키워드
- 해당 코드 블록에는 모니터락을 획득해야 진입이 가능
- 모니터 락을 가진 객체 인스턴스를 지정할 수 있음
- 해당 임계 영역에 들어가기 위해서는 파라미터 인스턴스가 모니터 락을 획득해야한다
```java
synchronized (object) {
    // critical section    
}
```
- 메소드에 선언하면 메소드 코드 블록 전체가 임계 영역으로 지정된다.
- 이때 모니터 락을 가진 객체 인스턴스는 this 객체이다.
```java
public synchronized void add() {
    // critical section    
} 
```

### wait() and notify()
- java.lang.Object에 선언되어 있어서 모든 자바 객체가 가진 메소드이다.
  - `자바에서 모든 객체는 내부적으로 모니터를 가진다.`
- 스레드가 어떤 객체의 wait() 메소드를 호출하면 해당 객체의 모니터 락을 획득하기 위해 대기 상태로 진입한다.
- 스레드가 어떤 객치의 notify() 메소드를 호출하면 해당 객체의 모니터에 대기중인 쓰레드 하나를 깨운다.
  - notifyAll() 메소드를 호출하면 대기중인 스레드 전부를 깨운다.

<br>

## 모니터는 언제 사용되나?
- 한번에 하나의 스레드만 실행되어야 할 때
- 여러 스레드와 협업이 필요할 때

## 모니터의 구성 요소
- `mutex`
  - critical section에 진입하려면 mutex lock을 취득해야한다.
  - mutex lock을 취득하지 못한 스레드는 큐에 들어간 후 대기(waiting) 상태로 전횐된다.
  - mutex lock을 획득한 스레드가 lock을 반환하면 락을 기다리며 큐에 대기 상태로 있던 스레드 중 하나가 실행
- `condition variables`
  - waiting queue를 가진다.
    - 조건이 충족되길 기다리는 스레드들이 대기 상태로 머무는 곳이다.
  - 자바의 모니터는 condition variables를 하나만 가진다.
  - 두가지 이상의 condition variable가 필요하다면 따로 구현이 필요하다.

### condition variables에서 주요 동작
- `wait`
  - 스레드가 자기 자신을 `condition variables`의 waiting queue에 넣고 대기 상태로 전환한다.
- `signal`
  - waiting queue에서 대기중인 스레드 중 하나를 깨운다.
- `broadcast`
  - waiting queue에서 대기중인 스레드 전부를 깨운다.

<br>

## 모니터의 두개의 큐
- entry queue : critical section에 진입을 기다리는 큐이다.
  - mutex에서 관리하는 큐
- waiting queue : 조건이 충족되길 기다리는 큐이다.
  - condition variables에서 관리되는 큐
  - 모니터가 notify() 해줄 때까지 기다리는 스레드를 관리하는 큐

### 모니터 예제 코드
- sudo
```text
acquire(m);         // 모니터의 락 취득
while(!p) {         // 조건 확인
    wait(m, cv)     // 조건 충족 안되면 waiting
}

... // 연산 수행

signal(cv); or broadcast(cv2) // 스레드를 깨운다. cv와 cv2는 같을 수 있음
release(m);     // 모니터 락 반환
```
- 위 예제를 통해 크게 두가지 상황을 볼 수 있다.
  1. 모니터 락 획득(acquire)과 반환(release)
  2. 조건에 따른 모니터 락 획득(acquire)과 반환(release)

<br>

- 모니터 락 획득(acquire)과 반환(release)
1. T1이 lock을 획득하여 critical section에 진입
2. T2가 진입하려는데 모니터 락을 소유한 스레드가 이미 있어서 Entry Queue에 대기
  
    ![img_1.png](img/monitor/img_1.png)

3. T1이 lock을 반환하고 release()을 통해 Entry Queue에서 대기하고 있는 스레드를 깨움.

<br>
<br>

- 조건에 따른 모니터 락 획득(acquire)과 반환(release)
1. T1이 lock을 획득하여 critical section에 진입
2. T2가 진입하려는데 lock을 획득하지 못해 Entry Queue에 대기

   ![img_1.png](img/monitor/img_1.png)

3. T1이 lock을 반환하고 release()을 통해 Entry Queue에서 대기하고 있는 T2 스레드를 깨움.
4. T2가 lock을 획득했지만 조건에 만족하지 못해 waiting queue에 들어가게 되면서 lock 반환

    ![img_2.png](img/monitor/img_2.png)

5. T3가 진입하여 lock을 획득하여 critical section에 진입
6. T4가 진입하려는데 lock을 획득하지 못하고 Entry Queue에 대기

    ![img_1.png](img/monitor/img_3.png)

7. T3 lock을 반환하려고 할 때 signal()을 통해 Waiting Queue에 있는 스레드를 깨워 다시 모니터 락 획득을 위해 Entry Queue로 이동

    ![img_1.png](img/monitor/img_4.png)

8. entry queue는 선입선출이 무조건이 아닌 OS 스케줄러가 어떤 선택을 하는지에 따라 대기하고 있는 스레드 중 하나가 다시 lock을 얻으려고 시도한다.
    - 이때 임계 영역에 있는 T3가 signal()로 T2를 깨우고 T3가 남은 작업을 계속 진행을 한다면 `signal and continue`라 하고
    - T3가 signal()로 T2로 깨우고 T3는 남은 작업을 남겨둔채 wait가 되고, T2가 시작하는 경우를 `signal and wait`라고 한다.
10. 운좋게 잠들었던 T2가 선택받았을 때 T2는 lock을 획득하고 T2가 멈췄던 wait(); 부분부터 다시 시작하게 된다.
11. 그리고나서 다시 조건이 충족하는지 비교하게 된다. 잠들었다가 깨어났는데 조건을 다시 판별하는 이유?
    - OS에 따라 wait()한 스레드가 이유없이 깰 수도 있다. 그래서 스레드가 기다렸던 조건이 충족했는지 판별해야한다.

    ![img_1.png](img/monitor/img_5.png)

<br>
<br>

## 컨슈머 프로듀서 문제
- 프로듀서와 버퍼, 컨슈머가 있다.
  - 프로듀서는 버퍼에 계속 item을 넣는 역할
  - 컨슈머는 버퍼에 있는 item을 소비하는 역할
- 버퍼를 공유해서 사용하는 경우 문제가 있다. 
  - 열심히 아이템을 생산해서 버퍼에 가득채웠는데 프로듀서가 버퍼에 빈공간이 있는지 계속 체크해줘야 한다.
  - 버퍼에 아이템이 없으면 컨슈머는 버퍼에 있는지 없는지 끊임없이 체크해줘야한다.
- 이런 문제를 모니터 락으로 해결할 수 있다.

```java
public class BoundedConsumerProducer {
    private static final Buffer q;
    private static final Mutex lock;
    private static final CV fullConditionVariables;
    private static final CV emptyConditionVariables;

    public void producer() {
        while (true) {
            lock.acquire();

            // 버퍼가 가득차면 lock을 반환하고 계속 produce 하지말고 waiting queue에서 대기하도록
            while (q.isFull()) {
                wait(lock, fullConditionVariables);
            }

            q.enqueue(item);

            // 제공한 다음 consumer의 waiting queue에서 잠자고 있는 스레드를 깨워서 item을 소비하도록 깨운다.
            signal(emptyCV);

            lock.release();
        }
    }


    public void consumer() {
        while (true) {
            lock.acquire();

            // 버퍼에 하나도 없으면 lock을 반환하고 계속 consume 하지말고 waiting queue에서 대기하도록
            while (q.isEmpty()) {
                wait(lock, emptyCV);
            }

            q.dequeue();
            // 소비한 다음 producer의 waiting queue에서 잠자고 있는 스레드를 깨워서 item 제공받도록 깨운다.
            signal(fullConditionVariables);

            lock.release();
        }
    }
}

```

<br>

- 위 코드를 동기화 시키면 아래와 같다.

```java
public class BoundedBuffer {
    private final int[] buffer = new int[5];
    private int count = 0;

    public synchronized void produce(int item) {
        while (count == 5) {
            wait();
        }

        buffer[count++] = item;
        notify();
    }
    
    public void consume() {
        int item = 0;
        // 파라미터 인스턴스인 자기자신의 mutex lock을 획득하고 임계영역으로 들어가라
        synchronized (this) {
            while (count == 0) {
                wait();
            }
            
            item = buffer[--count];
            notify();
        }
        System.out.println("consume :" + item);
    }
    
    public static void main(String[] args) {
        BoundedBuffer buffer = new BoundedBuffer();
        
        Thread consumer = new Thread(() -> {
            buffer.consume();
        });
        
        Thread producer = new Thread(() -> {
            buffer.produce(100);
        });
        
        consumer.start();
        producer.start();
        
        consumer.join();
        producer.join();
    }
}
```

