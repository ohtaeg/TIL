# Atomic
## Volatile

- 카운터 수치를 늘리는 등의 단순한 연산이 비원자적인 연산이라서 임게 영역을 정의하고 동기화를 사용해서 여러 스레드가 접근해도 연산을 보호해야한다.
- 어떤 작업이 원자적이고 비원자적인지 알 수 있는지 의문이기 때문에 보통 synchronized를 통해 방어적으로 공유 변수에 엑세스할 수 있는 모든 메서드를 동기화한다.
- 다만 synchronized를 사용하게 되면 한번에 한 스레드만 실행되다보니 한개의 스레드랑 다를게 없다. 
  - 동시 실행히 전혀 안되고 있고 여러 스레드를 위한 컨텍스트 스위칭 오버헤드가 존재


- 동기화를 최소화 하기 위해 어떤 작업이 원자적인지 알아야한다.
- 우리가 하는 대부분의 작업은 비원자적인 연산이지만 자바에서는 기본적으로 원자적인 연산을 보장하는 연산이 있다.
1. 레퍼런스를 참조하는 연산은 원자적인 연산이다.
```
Object a = new Object();
Object b = new Object();
a = b; // atomic
```

레퍼런스를 가져오거나 배열, 문자열 등 객체에 설정하는 getter/setter가 원자적으로 수행된다.

```java
public int[] getAges() {
    return this.age; // atomic
}

public void setName(String name) {
    this.name = name;  // atomic
}

public void setPerson(Person person) {
    this.person = person;  // atomic
}
```

2. primitive 타입에 대해 값을 할당하는 연산도 원자적인 연산이다.
```
int a = 10; // atomic
```
다만 **long 과 double은 예외이다.** 예외인 이유는 64비트라서 Java가 보장을 해주지 않기 때문이다.

자바 메모리 모델에 의하면 32비트 메모리에 값을 할당하는 연산은 중단이 불가능하다. (원자적이다)
그렇지만 long과 double의 경우에는 64비트의 메모리 공간을 갖고있기 때문에
64비트 컴퓨터인 경우 long double에 쓰기 작업을 하면 CPU가 두개의 연산으로 완료할 가능성이 높다.

[oracle - Non-Atomic Treatment of double and long](https://docs.oracle.com/javase/specs/jls/se9/html/jls-17.html#jls-17.7)



````
long x = 5;
long y = 10;
x = y; 
// x.lower_32_bits <- y.lower_32_bits 하위 32비트에 쓰고
// x.upeer_32_bits <- y.lower_32_bits 상위 32비트에 쓴다.
````

하위 비트 32비트 값을 할당한 후에 상위 32비트를 할당하기 직전에 다른 쓰레드가 끼어들어

두 32비트 값중 하나를 변경할 수 있기 때문에 long, double은 원자적 연산이 될 수 없다.

<br>

그렇다면 어떻게 원자적으로 연산을 할 수 있도록 할까?

`volatile` 키워드를 통해 한 개의 하드웨어 연산으로 수행하는 것을 보장한다.

`volatile` 키워드를 붙이면 **CPU가 변수를 읽어올때 CPU Cache Memory가 아닌 메인 메모리에서 직접 읽어오기 때문**

```
volatile double x = 1.0;
volatile double y = 9.0;

x = y // atomic
```