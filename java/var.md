# Local Variavle Type Interface (LVTI)

- java 10 부터 `지역 변수`에 지원하는 타입 인터페이스로써, `컴파일` 시점에서 **타입 추론**올 가능하게 해준다.
  - 런타임 오버헤드가 없다. 컴파일 시점에 이미 타입 추론을 하여 런타임 시점에 타입 확인을 위한 연산을 하지 않는다.

## 도입 배경

> We seek to improve the developer experience by reducing the ceremony associated with writing Java code, while maintaining Java's commitment to static type safety, by allowing developers to elide the often-unnecessary manifest declaration of local variable types.

- 자바는 정적 언어이기에 정적 타입에 대한 자바의 기존 약속을 유지하면서 
- 자바 코드 작성과 관련된 의식을 줄임으로써 개발자 경험을 개선

<br>
<br>


## 장점
### 가독성
- 지역 변수는 생성자를 사용하여 초기화되는 경우가 많기 때문에 이 부분에서 중복된 정보를 제거하는 `가독성`의 장점이 있다.
```java
// ORIGINAL
ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

// GOOD
var outputStream = new ByteArrayOutputStream();
```

<br>

### 람다식 파라미터 어노테이션 사용 가능
일반 람다의 경우 파라미터 어노테이션을 사용할 수 없다.

만약 어노테이션을 넣고 싶으면 따로 메소드로 빼던가, 익명 클래스로 정의해야 했었다.

자바 11부터는 타입 추론의 유연성을 추가하여 파라미터에 어노테이션 사용이 가능해졌다.

```java
// @Nonnull 어노테이션에 의해 person 파라미터는 널체크가 가능해진다.
Consumer<Person> personConsumer = (@NonNull var person) -> {};

```

<br>
<br>

## 단점
### 가독성
- 오히려 코드를 읽을 때 명시된 타입이 제거됨으로써 오히려 유용한 정보를 제거하는 가독성 저하가 있을 수도 있다.

<br>

### 잘못된 타입 추론
- 정수 값을 다룰 때 조심해야한다.
- **정수 타입들이 int로 추론될 수 있다.**
```java
// ORIGINAL
byte flags = 0;
short mask = 0x7fff;
long base = 17;

// DANGEROUS: all infer as int
var flags = 0;
var mask = 0x7fff;
var base = 17;
```

<br>

- `제네릭 타입`도 명시를 해야한다.
- **오른쪽에 선언시 제네릭 타입을 생략한다면 Object로 인식이 되어진다.**
```java
// ORIGINAL
PriorityQueue<Item> itemQueue = new PriorityQueue<Item>();

// DANGEROUS: infers as PriorityQueue<Object>
var itemQueue = new PriorityQueue<>();

// GOOD
var itemQueue = new PriorityQueue<Item>();
```
- List의 예제로 확인해보면 다음과 같다.
- **따라서 제네릭 사용시 컴파일러가 타입추론을 할 수 있도록 정보를 제공해줘야 한다.**
```java
// DANGEROUS: infers as List<Object>
var list = List.of();

// OK: infers as List<BigInteger>
var list = List.of(BigInteger.ZERO);
```

<br>


### IDE에 의존
- 타입 확인시 IDE에 의존하면 안된다.
- 개인적으로 타입 확인할 때 종종 IDE를 통해 도움을 많이 받곤한다.
  - var를 사용하게 되면 코드상에서 타입이 생략되기 때문에 타입을 확인하기 위해 var 변수위에 마우스 포인터를 올려 확인하는 등의 IDE를 통해 확인하는 경우가 있을 수 있다.
- 이런 경우는 var 변수 도입에 어긋나는 행위이다. 코드는 스스로 읽혀야하며, 도구의 도움없이 이해할 수 있어야한다.

<br>
<br>

## 주의해야할 점
### null 할당
- var 변수에는 null을 할당할 수가 없다.

> If the initializer has the null type, an error occurs—like a variable without an initializer, this variable is probably intended to be initialized later, and we don't know what type will be wanted.

컴파일 시점에 해당 var 변수가 나중에 초기화하는지, 어떤 유형을 원하는지 알 수 없기 때문

<br>
<br>
<br>

### Reference
- [JEP-286](https://openjdk.org/jeps/286)
- [백선장님 유튜브](https://www.youtube.com/watch?v=tjj-XLk4CSA&ab_channel=%EB%B0%B1%EA%B8%B0%EC%84%A0)
