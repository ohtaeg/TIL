# Synchronized
## synchronized block

synchronized method의 문제는 인스턴스 레벨의 잠금이 획득되어 인스턴스에 포함된 모든 synchronized의 접근까지 lock이 걸리게 되는 문제가 있다.

아래와 같이 코드에 synchronized 블록을 정의하여 **전체 메서드를 동기화하지 않으면서 해당 `인스턴스`에 대해서만 접근을 제한할 수 있다.**

즉 인스턴스 단위로 락을 건다. 

```java
public class SynchronizedBlock {
    Counter lockingObject = new Counter();
    Counter otherLockingObject = new Counter();
    
    public void foo1() {
        // Concurrent execution
        
        // Non-Concurrent execution
        synchronized(lockingObject) {
            // thread A
        }

        // Concurrent execution
    }

    public void foo2() {
        // Concurrent execution

        // Non-Concurrent execution
        synchronized(otherLockingObject) {
            // thread B
        }

        // Concurrent execution
    }

    public void foo3() {
        synchronized(this) {
            
        }
    }
}
```
위와 같이 객체 내부에 서로 다른 동기화된 임계 영역을 만들어 (foo3)

락의 역할을 하게 될 동기화할 객체를 만들고 한 개의 스레드만 실행을 허용하도록 유연성을 높일 수 있다.

게다가 스레드간 동기화가 필요한 코드를 더 적게할 수 있다.
(Concurrent execution, Non-Concurrent execution)

## static synchronized block
```java
public class SynchronizedStaticBlock {
    public static void foo1() {
        // lockingObject
        synchronized(Counter.class) {
            
        }
    }

    public static void foo2() {
        // otherLockingObject
        synchronized(Counter.class) {

        }
    }
}
```
static 메서드에 동기화 블럭을 사용하는 경우 **synchronized method처럼 A가 foo1()에 진입을 하였다면 B는 foo2()에 진입하지 못한다.**

이때 클래스 단위로 락이 잡혀 같은 객체의 서로 다른 주소 값을 가진 인스턴스를 던져도 A 스레드가 나와야 B 스레드가 진입이 가능하기 때문이다.
(SynchronizedStaticBlock 예제 참고)