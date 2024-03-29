# Synchronization

### 하나의 객체를 두 개의 스레드가 접근할 때 생기는 일은?
- 다음과 같은 객체가 있다고 가정해보자
```java
public class Counter {
    private int state = 0;
    public void increment() {
        this.state++;
    }
}
```

- **싱글 코어**에서 **두 개의 쓰레드**가 increment()를 계속 호출한다고 가정했을 때

  ![img.png](img/synchronization/img.png)

<br>

- CPU 레벨에서는 state++; 구문이 아래 어셈블리어로 변환되면서 동작한다. 
```text
R1 - 레지스터, CPU 안에 포함된 데이터 저장소

LOAD state to R1   <- 메모리에 있는 state 변수 값을 레지스터 R1로 적재 
R1 = R1 + 1        <- 레지스터에 있는 값을 +1 해서 다시 레지스터에 저장
STORE R1 to state  <- 레지스터에 있는 값을 메모리에 있는 state 변수로 저장 
```

<br>

### 만약 두 스레드가 동시에 ++ 연산을 수행하게 되면 어떤 현상이 생길 수 있을까?

1. T1이 increment()를 먼저 수행했을 때 CPU 레벨에서는 `LOAD state to R1`을 실행
2. T1이 `R1 = R1 + 1` 을 실행, 그럼 현재 상황은 다음과 같다.
  ![img.png](img/synchronization/img_1.png)

3. 이때 컨텍스트 스위칭이 발생해서 T2가 시작하게되어 increment()가 시작되면 다시 CPU 레벨에서 `LOAD state to R1`을 실행
   - 이때 메모리에 있는 state 값은 0이기 때문에 R1에 0이 올라가게 된다.
  
   ![img_1.png](img/synchronization/img_2.png)

4. 그리고나서 T2는 계속 `R1 = R1 + 1` 명령어 수행후 `STORE R1 to state`를 수행하게 되면 메모리에 있는 state 값은 0 -> 1로 바뀔 것이다.
  ![img_2.png](img/synchronization/img_3.png)
5. 그리고나서 컨텍스트 스위칭이 발생해서 T1으로 돌아온다.
6. T1은 다음 작업인 `STORE R1 to state`를 수행하면 R1에 마지막 상태 값인 1 값을 로딩을 하여 메모리에 적재한다.
  ![img_3.png](img/synchronization/img_4.png)

<br>

- **결과적으로 쓰레드 스스로가 작업하던 값이 아닌 다른 값으로 연산을 할 수 있다!**
  - 즉 T1이 작업하던 데이터가 컨텍스트 스위칭이 발생하여 메모리에 써지지 않은 상태에서 <br> T2가 시작이 되어 T1이 반영한 값으로 작업을 하지 못함
  
- 하나의 쓰레드에서 언제 컨텍스트 스위칭이 발생하냐에 따라 결과가 달라질 수 있다.

<br>
<br>

## Race Condition (경쟁 조건)
- 여러 프로세스 / 스레드가 동시에 같은 데이터를 조작할 때 컨텍스트 스위치, 타이밍이나 접근 순서에 따라 결과가 달라질 수 있는 상황을 뜻한다.


## Synchronization
- 여러 프로세스 / 스레드가 동시에 실행해도 공유 데이터의 일관성을 유지하는 것을 동기화라고 한다.
- 위 코드를 어떻게 동기화 시킬 것인가?
  - 컨텍스트 스위칭이 발생하지 않도록 하면 되지 않을까?
    - 싱글 코어에서만 가능하고 멀티 코어에서는 불가능하다.
  - 하나의 스레드만 실행하도록 하면 되지 않을까?
    - 멀티 코어인 경우 하나의 쓰레드에서 이미 수행중이기 때문에 다른 코어의 쓰레드가 기다려야하기에 가능

## Critical Section 
- 공유 데이터의 일관성을 보장하기 위해 하나의 프로세스 / 스레드만 진입해서 실행 가능한 영역을 임계 영역이라고 한다.
- 락을 쥐고 나서부터 락을 해제하기 전까지의 영역
- 임계 영역 접근에 대한 조건은 3가지가 있다.
1. mutual exclustion (상호 배제)
    - 한번의 하나의 프로세스 / 스레드만 임계 영역에서 실행 할 수 있어야 한다.
2. progress (진행)
    - 만약 임계 영역에서 수행중인 프로세스 / 스레드가 없고 어떤 프로세스 / 스레드가 임계 영역에 들어가길 원한다면 그 중에 하나는 실행될 수 있어야한다.
3. bounded waiting (한정된 대기)
    - 어떤 프로세스 / 스레드가 임계 영역에 들어가지 못해 무한정 대기하고 있으면 안된다.

<br>

## 어떻게 해야 mutual exclusionn을 보장할 수 있을까?
- 락을 사용한다. 여러 스레드가 공유해서 쓰는 데이터를 보호할 때 사용된다.
- 대부분의 락은 CPU 도움이 필요하며, CPU의 도움이 필요한 대표적인 락들은 아래와 같다
  - `스핀락`, `뮤텍스`, `세마포어`

<br>

## 스핀락
- 락을 가질 때까지 반복해서 락을 쥐려고 시도하는 락
  - 반복문에서 계속 확인을 하기 때문에 CPU를 계속 사용하게 된다는 특징이 있다.
  - 즉, 기다리는 동안 CPU를 낭비하는 단점이 있다.
- 예제로 간단하게 살펴본다.
  - 여러 쓰레드가 글로벌 변수 lock에 대한 접근을 할 때 루프를 통해 lock을 얻기 위해 시도하는 예제이다.
```
volatile int lock = 0;      // global 변수

void test() {
    while (test_and_set(&lock) == 1);  // 반복 시도, CPU 계속 사용
        // critical section 
    lock = 0;
}

int test_and_set(int* lockPtr) {
	int oldLock = *lockPtr;  // 기존 값 저장
	*lockPtr = 1;            // 1로 바꿈
	return oldLock;          // 기존 값 반환
}
```
1. 쓰레드 T1이 lock을 획득하며 임계 영역에 들어가게 된다. 이때 lock 데이터는 1이 된다.

  ![img.png](img/synchronization/spinlock.png)

2. 그리고 나서 T2가 시작되면 while문의 조건을 확인하면서 임계 영역에 들어가지 못하고 무한 루프를 통해 lock을 계속 얻으려고 획득하게 된다. 
  
  ![img_1.png](img/synchronization/spinlock_1.png)

3. T1이 작업을 끝내고 0을 반환하게 되면 T2가 임계 영역에 들어가게 된다.

  ![img_2.png](img/synchronization/spinlock_2.png)

<br>

- 만약에 T1과 T2에 동시에 tset_and_set()을 호출하거나 중간에 컨텍스트 스위칭을 하게 되면 둘 다 0이라는 값을 가져 둘다 임계 영역에 들아갈 수 있지 않을까?
  
  ![img_3.png](img/synchronization/spinlock_3.png)

  - 맞다. 그래서 test_and_set() 함수는 위에서 설명했듯이 **cpu의 atomic 명령으로써 도움을 받아야한다.**
  
- Atomic 명령어
  - 실행 중간에 간섭받거나 중단되지 않는다.
  - 같은 메모리 영역에 대해 동시에 실행되지 않는다.
    - 즉, test_and_set() 함수를 두 쓰레드가 동시에 실행할지라도 CPU 레벨에서 하나를 먼저 실행하고 다른 하나를 실행시키도록 동기화를 한다. 

<br>

----

## 뮤텍스
- **mut**ual + **ex**clusion 을 합친 말
- 락을 가질 수 없으면 가질 수 있을 때 까지 휴식, 락을 쥘 수 있게되면 그때 깨어난다.
  - 이때 컨텍스트 스위칭이 발생한다.
- 즉, 임계 영역에 들어갈 때 자기가 락을 걸어 다른 프로세스 / 스레드가 접근하지 못하도록 하고 작업이 끝나면 직접 락을 해제한다.
  - 다른 스레드가 공유자원에 접근하면 Blocking 후 대기 큐로 보내고 락을 해제하면 대기 큐에 있는 스레드를 데려온다.
- 오직 1개의 프로세스 / 스레드만 접근할 수 있다.
  


### 뮤텍스가 스핀락보다 항상 좋은걸까?
- 항상 그렇지 않다. 스핀락이 더 좋은 경우도 있다.
- 멀티코어 환경에서 임계 영역에서의 작업이 컨텍스트 스위칭보다 더 빨리 끝나면 스핀락이 뮤텍스보다 더 이점이 있다.
  - 뮤텍스는 휴식하거나 깨울 때 컨텍스트 스위칭이 발생하는데 스핀락은 락을 쥘 때까지 계속 확인한다.
- 꼭 멀티 코어에서 이점이 있는건가?
  - 싱글 코어 환경이라면 코어가 하나기 때문에 스핀락 방식이라해도 이미 다른 스레드가 락을 쥐고 있어서 잠들기 때문에 컨텍스트 스위칭이 발생할 수 밖에 없어서 이점이 없다.


<br>
<br>

### SimpleDateFormat.class
- 여담으로 자바에서 대표적인 thread-unsafe한 대표적인 클래스이다.
- 문서를 읽어보면 다음과 같이 되어있다.
```text
 * Date formats are not synchronized.
 * It is recommended to create separate format instances for each thread.
 * If multiple threads access a format concurrently, it must be synchronized externally.
```

<br>

### Java는 synchronized 키워드를 이용해 이미 동시성 제어가 가능한데 Atomic 클래스를 왜 제공하는거지?

|          | synchronized                                     | Atomic            |
|----------|--------------------------------------------------|-------------------|
| 동기화 범위   | 락 획득/해제는 반드시 메서드 수준이나 메서드 내부의 동기화 블록 안에서 이루어져야 함 | 훨씬 작은 범위에 동기화를 보장 |
| lock     | O                                                | X (CAS 알고리즘)      |
| Blocking | Blocking                                         | Non-Blocking      |

- synchronized
  - 락 획득/해제는 반드시 메서드 수준이나 메서드 내부의 동기화 블록 안에서 이루어져야 함
  - synchronized 블록에 진입하기 전에 CPU 캐시 메모리와 메인 메모리 값을 동기화하여 가시성을 해결
  - 단점은 락을 얻지 못한 스레드는 블로킹됨, 서비스에 트래픽이 발생할 경우 심각한 성능 저하가 발생할 수 있다
- Atomic 클래스
  - 훨씬 작은 범위에 동기화를 보장하고, volatile의 Read-Modify-Write 문제를 해결할 수 있다.
  - CompareAndSwap 알고리즘을 이용해서 락 프리하면서 NonBlocking으로 동작
    - 이로인해 synchronized보다 효율적으로 동시성 및 원자성 보장
    - CAS 알고리즘은 쓰레드가 가지고 있던 원래 값이 현재의 값과 같은 지 비교하고, 같으면 그냥 사용하고 다르면 현재의 값을 받아온다.
  - 멀티쓰레드에서 write도 가능
  - 다만 내부에서 루프를 이용해 CAS 작업을 반복적으로 재시도하기 때문에 비교 후 업데이트하는 작업이 실패할 경우를 대비해 내부적인 재시도 루프가 동반됨
    - 변수를 업데이트하기 위해 여러 차례 재시도를 해야 할 경우, 그 횟수만큼 성능이 나빠짐

### 동기화 처리 기법 중 성능이 뛰어난 Atomic을 사용하면 되지 않나?
- 비교적 성능이 좋은 Atomic을 일반적으로 사용할 순 있지만 만능은 아니라고 생각한다. 상황에 따라 적절한 동기화 기법을 사용하면 될 것 같다.
  - synchronized 는 스레드가 suspending과 resuming 하는데에 자원 소모가 발생하고,
  - Atomic은 CAS 알고리즘을 이용해 성공적으로 변경할 때까지 무한 루프를 돌면서 자원 소모가 발생한다.
  - volatile의 경우는 하나의 스레드가 쓰기 연산을 하고, 다른 스레드에서는 읽기 연산을 통해 최신 값을 가져올 경우. 즉 다른 스레드에서는 업데이트를 행하지 않을 경우 이용할 수 있다.
    - 쓰레드가 CPU Cache에 저장된 값을 변경하고 메모리에 제때 쓰지 않는다면 각 쓰레드에서 사용하는 변수값이 일치하지 않을 수가 있다.
  - Atomic 클래스의 경우는 CAS 알고리즘을 이용하여 여러 스레드에서 읽기 쓰기 모두 이용할 수 있다.
  - synchronized 경우도 여러 스레드에서 읽기 쓰기 모두 이용할 수 있자만 Lock을 사용한다.
- [문서](https://docs.oracle.com/javase/8/docs/api/index.html?java/util/concurrent/atomic/package-summary.html)에서도 Atomic 클래스들은 다음과 같이 설계 되어있다고 한다.
```text
Atomic classes are designed primarily as building blocks for implementing non-blocking data structures and related infrastructure classes.
The compareAndSet method is not a general replacement for locking. It applies only when critical updates for an object are confined to a single variable.

Atomic 클래스는 인프라 클래스들과 관련되어있고 non-blocking 데이터 구조를 구현하기 위해 설계되었다.
CommpareAndSet 메서드는 락의 대체가 아니며,
중요한 업데이트가 단일 변수로 제한되는 경우에만 적용된다.
```
- 즉, 개인적으로 느끼기엔 멀티 쓰레드 환경에서 많은 데이터에 대한 동시성을 관리하기에는 오히려 부적합해보인다.
- 게다가 ConcurrentHashMap은 CAS와 synchronized 두 개의 방식 모두 사용하고있다. 필요한 상황이 되면 맞는 동기화 방법을 택하면 될 것 같다.
  - 어떤 상황에 어떤 동기화가 적절할지는 아직 감이 오질 않는다. 경험과 공부가 더 필요해보인다.