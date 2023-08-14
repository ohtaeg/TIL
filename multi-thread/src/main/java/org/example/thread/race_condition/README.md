# Synchronized
JVM은 여러개의 스레드가 하나의 자원을 동시 접근할 때 임계 영역을 지켜주는 기법을 제공한다.

[모니터락](https://github.com/ohtaeg/TIL/blob/master/os/monitor.md)

여러 개의 스레드가 코드 블록이나 전체 메서드에 엑세스할 수 없더록 설계된 락킹 메커니즘

## synchronized method
```java
public class ClassWithCriticalSection {
    public synchronized void foo1() {
        // thread A
    }

    public synchronized void foo2() {

    }
}
```

위와 같은 경우 스레드 A, B가 있을 때 

A가 먼저 foo1()에 진입하고 바로

B가 바로 foo2()에 진입하려고 할 때 **B는 foo2() 및 foo1()에 접근을 못한다.**

이유는 synchronized는 자신이 포함된 객체(this)에 lock을 건다.

즉 스레드 A가 먼저 foo1()에 진입함으로써 해당 객체에 포함된 모든 synchronized의 접근 까지 lock이 걸리기 때문

<br>

## synchronized block

위와 같은 해결을 할 수 있는 방법은 synchronized block을 이용하는 것이다.

아래와 같이 코드의 블록을 정의하고 synchronized 키워드를 이용해 전체 메서드를 동기화하지 않으면서 해당 영역에 대해서만 접근을 제한할 수 있다.

```java
public class ClassWithCriticalSection {
    A lockingObject = new A();
    
    public void foo1() {
        // Concurrent execution
        
        // Non-Concurrent execution
        synchronized(lockingObject) {
            // thread A
        }

        // Concurrent execution
    }

    public void foo2() {
        synchronized(this) {
            // thread B
        }
    }
}
```
위와 같이 객체 내부에 서로 다른 동기화된 임계 영역을 만들어 

락의 역할을 하게 될 동기화할 객체를 만들고 한 개의 스레드만 실행을 허용하도록 유연성을 높일 수 있다.

게다가 스레드간 동기화가 필요한 코드를 더 적게할 수 있다.
(Concurrent execution, Non-Concurrent execution)

<br>

## Reentrant
Synchronized 메서드, Synchronized 블록은 스레드가 재진입할 수 있다.

스레드 A가 이미 Synchronized 메서드, 블록에 있는 상태에서 다른 Synchronized 메서드, 블록에 엑세스할 수 있다.

즉, 기본적으로 스레드는 자신이 임계 영역에 진입하는 것을 막지 못한다.