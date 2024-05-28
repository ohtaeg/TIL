# ConcurrentHashMap
- Hashtable, HashMap의 단점을 보완하면서 Multi-Thread 환경에서 동시성을 보장할 수 있는 자료구조

## Hashtable, HashMap의 단점
해시 테이블의 단점은 다음과 같다.

```java
public class Hashtable<K,V>
    extends Dictionary<K,V>
    implements Map<K,V>, Cloneable, java.io.Serializable {

    public synchronized V get(Object key);
    public synchronized V put(K key, V value);
}
```
메서드 단위로 [모니터 락](https://github.com/ohtaeg/TIL/blob/master/os/monitor.md)을 사용하기 때문에 다른 스레드들이 메서드 접근시 대기하게 될 수 있다.

<br>

해시 맵의 단점은 다음과 같다.

```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable {

    public V get(Object key) {}
    public V put(K key, V value) {}
}
```
메서드 시그니처 및 내부에서도 별도로 lock을 걸지 않기 때문에 Multi-Thread 환경에서 동시성을 보장할 수 없다.

<br>

## ConcurrentHashMap의 스레드 세이프
ConcurrentHashMap은 메서드 내부에서 synchronized 및 CAS 알고리즘을 사용한다.

```java
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>
    implements ConcurrentMap<K,V>, Serializable {

    final V putVal(K key, V value, boolean onlyIfAbsent) {
        for (Node<K,V>[] tab = table;;) {
            if (tab == null || (n = tab.length) == 0) {
                // ...
            } else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
                if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value)))
                    break;                   // no lock when adding to empty bin
            } else {
                synchronized (f) {
                    // ...
                }
            }
        }
    }
}
```

- **CAS**를 사용하는 경우는 버킷이 아예 없는 경우 **새로운 버킷을 만들며 노드를 추가**할 때 사용하고
- **synchronized**를 사용하는 경우 **이미 버킷이 존재하여 노드를 추가하는 경우**이다.


## 왜 동일한 동기화 기법을 사용하지 않았지?
- synchroized를 사용하면 올바른 결과를 얻지만 느린 속도로 성능 저하의 원인이 될 수 있다.
- CAS 알고리즘은 락 프리하여 데드락과 같은 문제를 방지하면서도 높은 성능을 유지할 수 있지만 복잡한 구조이다.

[옛날에 내가 정리했던 글을 인용](https://github.com/ohtaeg/TIL/blob/master/os/synchronization.md#%EB%8F%99%EA%B8%B0%ED%99%94-%EC%B2%98%EB%A6%AC-%EA%B8%B0%EB%B2%95-%EC%A4%91-%EC%84%B1%EB%8A%A5%EC%9D%B4-%EB%9B%B0%EC%96%B4%EB%82%9C-atomic%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%98%EB%A9%B4-%EB%90%98%EC%A7%80-%EC%95%8A%EB%82%98)하여 생각을 해보면

Atomic한 연산(CAS)은 많은 데이터의 동시성을 보장하는 연산을 위해 탄생한 것이 아닌 Non-blocking 하게 데이터 구조를 다루기 위해 그리고 단일 변수, 연산에 제한을 위해 탄생하였기 때문에

**새로운 버킷을 만들며 노드를 추가하는 경우는 단순 버킷 생성과 단일 노드 추가이기 때문에 여러 스레드가 자원 접근을 위해 경합할 필요가 없어서 CAS를 사용하고**

**이미 버킷이 존재하여 노드를 추가하는 경우 버킷에 얼마나 많은 노드가 있는지 모르기도하고 버킷 접근시 Race condition이 일어날 수도 있어 synchronized를 사용한 잠금 기법을 사용하여**

두개의 기법을 사용하지 않았나 싶다.

> 동시성 문제를 해결하지 않으면 데이터 손상, 교착 상태(deadlock) 또는 경쟁 조건(race condition)과 같은 문제가 발생할 수 있다.

<br>


## size()
ConcurrentHashMap은 HashMap과 또 다른 차이가 있는데 바로 size를 구하는 부분이다.

HashMap은 size() 내부는 별도의 필드를 통해 바로 size 값을 반환하지만
```java
public class Hashtable<K,V>
    extends Dictionary<K,V>
    implements Map<K,V>, Cloneable, java.io.Serializable {
    
    transient int size;
    
    public final int size()                 { return size; }
}
```

ConcurrentHashMap은 별도의 필드가 없고 반복문을 통해 size를 반환한다. 그로 인해 HashMap보다 오버헤드가 있다.

```java
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>
    implements ConcurrentMap<K,V>, Serializable {

    public int size() {
        long n = sumCount();
        return ((n < 0L) ? 0 :
                (n > (long)Integer.MAX_VALUE) ? Integer.MAX_VALUE :
                        (int)n);
    }
}
```

## 내부에 Atomic 필드를 선언하여 size 계산을 하면 되지 않을까?
아무래도 자바 개발자들이 Atomic 변수를 왜 선언하지 않았을까 고민을 해보다가 2가지 이유로 인해 사용하지 않은 것 같다.

#### 1. 내부에 변수를 두는 것은 의미가 있을 수도, 없을 수도 있다고 생각이 문득 들었다.
가령 ConcurrentHashMap에 업데이트를 수행하고 있는 10개의 스레드가 있고, 11번째 스레드가 크기를 구하려고 할 때

11번째 스레드가 읽는 숫자는 다른 10개 스레드의 실행 타이밍에 전적으로 달려 있기 때문에

11번째 스레드가 수행할 때 데이터 갯수가 10개가 아닐 수도 있기에 Atomic 변수를 사용하는 것이 도움이 되지 않을수도 있어서 별도로 선언하지 않았나 싶기도 하다.

#### 2. 성능 저하
Atomic 연산이 동기화 기법보다 빠르다지만 추가적인 연산이 수행되는 것이기 때문에 업데이트 연산을 수행하는 모든 스레드들이 강제로 Atomic 변수를 업데이트하는 경우 성능에 조금이나마 영향을 끼칠 수 있지 않을까?


1, 2번의 이유로 인해 굳이 만들지 않은 것 같다.


 






