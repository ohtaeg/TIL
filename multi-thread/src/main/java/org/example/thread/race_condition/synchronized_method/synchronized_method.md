# Synchronized
## synchronized method
```java
public class SynchronizedMethod {
    public static void main(String[] args) {
        SharedClass sharedObject = new SharedClass();

        Thread thread1 = new Thread(() -> sharedObject.foo1());
        Thread thread2 = new Thread(() -> sharedObject.foo2());

        thread1.start();
        thread2.start();
    }

    static class SharedClass {
        public synchronized void foo1() {
            // thread A
        }

        public synchronized void foo2() {

        }
    }
}
```

위와 같은 경우 스레드 A, B가 있을 때 

A가 먼저 foo1()에 진입하고 바로

B가 바로 foo2()에 진입하려고 할 때 **B는 foo2() 및 foo1()에 접근을 못한다.**

이유는 synchronized 메서드는 **자신이 포함된 인스턴스(this)에 lock을 건다.**

**즉 스레드 A가 먼저 foo1()에 진입함으로써 해당 인스턴스에 포함된 모든 synchronized의 접근까지 lock이 걸리기 때문**

<br>


> 위와 다르게 같은 타입의 **서로 다른 인스턴스**인 경우 같은 메서드 or 서로 다른 메서드를 호출하면 어떻게 될까?
```java
public class SynchronizedMethod {
    public static void main(String[] args) {
        SharedClass sharedObject1 = new SharedClass();
        SharedClass sharedObject2 = new SharedClass();

        // 서로 다른 인스턴스 같은 foo1() 호출
        Thread thread1 = new Thread(() -> sharedObject1.foo1());
        Thread thread2 = new Thread(() -> sharedObject2.foo1());

        // or

        // 서로 다른 인스턴스 각각 foo1(), foo2() 호출
        Thread thread1 = new Thread(() -> sharedObject1.foo1());
        Thread thread2 = new Thread(() -> sharedObject2.foo2());

        thread1.start();
        thread2.start();
    }

    static class SharedClass {
        public synchronized void foo1() {
        }

        public synchronized void foo2() {
        }
    }
}
```
서로 다른 인스턴스이기에 같은 메서드를 호출하더라도 스레드 1, 2가 foo1()에 진입할 수 있다.

혹은

서로 다른 인스턴스이기에 다른 메서드를 호출하더라도 스레드 1, 2가 foo1(), foo2()에 진입할 수 있다.

<br>

## static synchronized method
```java
public class SynchronizedStaticMethod {
    public static void main(String[] args) {
        SharedClass sharedObject = new SharedClass();

        Thread thread1 = new Thread(() -> sharedObject.foo1());
        Thread thread2 = new Thread(() -> sharedObject.foo2());

        thread1.start();
        thread2.start();
    }

    static class SharedClass {
        public static synchronized void foo1() {
        }

        public static synchronized void foo2() {
        }    
    }
}
```
synchronized method에 static 키워드가 붙었을 때 스레드 A, B는 어떻게 될까?

**synchronized method처럼 A가 foo1()에 진입을 하였다면 B는 foo2()에 진입하지 못한다.**

**다만 다른 인스턴스더라도 인스턴스가 아닌 클래스 단위로 lock을 거는 차이가 있다.**

instance-level lock이 아닌 `class-level lock`

<br>

> static synchronized method 와 synchronized method가 혼용되어 있다면?
```java
public class SynchronizedStaticMethod {
    public static void main(String[] args) {
        SharedClass sharedObject = new SharedClass();

        Thread thread1 = new Thread(() -> sharedObject.foo1());
        Thread thread2 = new Thread(() -> sharedObject.foo2());

        thread1.start();
        thread2.start();
    }

    static class SharedClass {
        public static synchronized void foo1() {
        }

        public synchronized void foo2() {
        }
    }
}
```
thread A는 foo1() 진입

thread B는 foo2()에 진입이 된다.

인스턴스 단위의 락과 클래스 락이 각각 별개로 잡힌걸까?

개인적으로 추측컨데 `class-level lock`이 잡혔기에 좁은 범위인 instance-level lock은 안잡혔다고 생각한다.

즉 foo1()은 동기화 되어있지만 foo2()는 동기화 되어있지 않은 것 같다.