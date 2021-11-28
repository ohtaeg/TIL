# 싱글톤 패턴

### Goal
- 싱글톤이란
- 싱글톤 패턴 구현 방법들
- 싱글톤 패턴 vs 정적 클래스

<br>

------


## 싱글톤 패턴이란?
- 최초 한번만 메모리에 할당하여 생성하여 인스턴스가 **오직 단 하나만 생성되는 것**을 보장하고, **어디에서든 이 인스턴스에 접근**할 수 있는 패턴

![sequence-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter6_singleton/uml/singleton_sequence.puml)

<br>

## 싱글톤 구현 방법
- 프린터기를 한대만 생성하고 싶은데..?
```java
public class Printer {
  public Printer() {
  }
  
  public void print(Resource r) {
    
  }
}
```

<br>

- 우선 외부에서 생성자를 호출하지 못하도록 막자

```java
public class Printer {
  private Printer() {
  }
  
  public void print(Resource r) {
    
  }
}
```

<br>

- 외부에서 접근 못하게 했다. 이제 최초 한번만 생성하여 외부로 제공하도록 하자.

```java
public class Printer {
  private static Printer printer = null;
  
  private Printer() {
  }
  
  public static Printer getPrinter() {
    if (printer == null) {
      printer = new Printer();
    }
    return printer;
  }
  
  public void print(Resource r) {
    
  }
}
```

<br>

- 그러나 두 개 이상의 멀티 스레드 환경에서 인스턴스를 획득하기 위해 getPrinter()메서드에 진입하여 경합을 벌이는 과정에서 <br>
서로 다른 두 개의 인스턴스가 만들어지는 형태가 발생할 여지가 있다.
  - racing 문제
  - 일관된 상태를 유지해야하는데 유지하지 못하는 상황이 생긴다.
  - [racing 예제 코드](./example/racing)

<br>

### 해결 방법
1. 정적 변수에 인스턴스를 만들어 바로 초기화 하는 방법
  - 인스턴스를 필요할 때 생성하지 않고, 처음부터 인스턴스를 만들어 버린다
```java
public class Printer {
    private static Printer printer = new Printer();
    private int count = 0;

    private Printer() {
    }

    public static Printer getPrinter() {
        return printer;
    }

    public void print(Resource r) {
        count++;
    }
}
```
- 이 방법의 단점은 인스턴스를 미리 만들기 때문에 사용하지 않으면 불필요한 시스템 리소스를 낭비할 가능성이 있다.
- 또한 일관된 상태를 유지할 수 없다.

<br>

2. 인스턴스를 만드는 메서드에 동기화 하는 방법
```java
public class Printer {
    private static Printer printer = null;
    private int count = 0;

    private Printer() {
    }

    public synchronized static Printer getPrinter() {
        if (printer == null) {
            printer = new Printer();
        }
        return printer;
    }

    public void print(Resource r) {
        synchronized (this) {
            count++;    
        }
    }
}
```

<br>

3. Double-Checked-Locking
```java
public class Printer {
    private volatile static Printer printer = null;
    private int count = 0;

    private Printer() {
    }

    public static Printer getPrinter() {
        if (printer == null) {
            synchronized (Printer.class) {
                if (printer == null) {
                    printer = new Printer();        
                }
            }
        }
        return printer;
    }

    public void print(Resource r) {
        synchronized (this) {
            count++;    
        }
    }
}
```
- 첫번째 if문을 통해 인스턴스가 null인지 확인하고 동기화를 걸어 다시 한번 더 체킹하는 방법
  - 기존 방법은 쓰레드 A, B가 첫번째 if문을 동시에 접근했을 때 인스턴스가 null 이였기에 인스턴스 2개 이상을 생성하는게 문제
- 첫번째 if 문에서 null임이 확인된 이후에는 synchronized 블럭으로 넘어가게 되는데, 이 때는 이 블럭이 단 하나의 쓰레드만 내부로 접근할 수 있도록 막는다.
- 동기화를 해버리면 성능 저하 이슈를 고려해야한다.
- 생성하는 부분과 상태값이 변경되는 부분에 동기화를 신경써줘야 한다.

<br>

4. LazyHolder
- 동기화가 필요 없는 방법. (다르게 말하면 JVM에게 동기화를 위임하는 방법)
- inner class를 참조하는 순간 Class가 로딩되어 초기화가 진행된다.
  - 클래스 초기화 시점은
  - 클래스의 인스턴스가 생성될 때
  - 클래스에서 선언한 static 메소드가 호출되었을 때
  - 클래스에서 선언한 static 변수에 값이 할당되었을 때
  - 클래스에서 선언한 static 변수가 상수가 아니면서 사용 되었을 때
- `Class.forName()`이 어떻게 동작하는지 알아보면 이해가 쉽다.
- inner class 안에 선언된 LazyHorder.INSTANCE가 static이기 때문에 클래스 로딩 시점에 한번만 호출된다.
- 이 방법은 JVM을 통해 초기화가 진행되므로 JVM 클래스 초기화 과정에서 thread-safe 하다.


```java
public class Printer {
    private int count = 0;

    private Printer() {
    }

    public static Printer getPrinter() {
        return LazyHorder.INSTANCE;
    }

    public void print(Resource r) {
        count++;
    }
    
    private static class LazyHolder {
        private static final Printer INSTANCE = new Printer();
    }
}
```

<br>

5. 번외 리플렉션 방어
- 리플렉션을 통해 싱글톤을 무시할 수 있다.
- 생성자에 예외처리를 추가하도록 하여 리플렉션 방어도 해주자.
- [리플렉션을 통한 싱글톤 부시기 코드](./example/reflect)

```java
public class Printer {
    private volatile static Printer printer = null;
    private int count = 0;

    private Printer() {
        if ( instannce != null ) {
            throw new ExceptionInInitializerError( "생성자를 통해 접근할 수 없습니다." );
        }
    }

    public static Printer getPrinter() {
        if (printer == null) {
            synchronized (Printer.class) {
                if (printer == null) { 
                    printer = new Printer(); 
                } 
            }
        }
        return printer;
    }

    public void print(Resource r) {
        synchronized (this) {
            count++;
        }
    }
}
```

<br>
<br>

## 싱글톤 패턴 vs 정적 클래스
- 정적 클래스란?
  - static 메서드로만 이루어진 클래스
```java
public class Printer {
    private static int count = 0;
    
    public synchronized static void print(final String text) {
        count++;
        System.out.println(text + count);
    }
}
```
- 싱글톤 패턴과 차이점은 인스턴스를 생성하지 않고 메서드를 사용하는 점
- 스레드가 안전하게 공유되어 사용될 수 있다.

<br>

- 정적 메서드는 런타임 환경에서 다형성을 사용할 수 없다.
  - 즉 오버라이딩을 구현할 수 없다.
```java
public interface Printer {
    static void print(String text); 
}

public class RealPrinter implements Printer {
    public synchronized static void print(String text) {
        // ...
    }
}
```

<br>

- 다음 코드를 보고 생각해봅시다.
```java
public class Printer {
    
    private final PrintingService service;
    
    public Printer(final PrintingService service) {
        this.service = service;
    }
    
    public void foo() {
        // 1
        service.print();
        
        // 2
        PrintUtils.print();
  }
}
```
- 1번은 메서드이고, 2번은 함수이다.
- 메서드와 함수의 차이가 무엇인지 같이 고민해보면 좋을 것 같다.

<br>

- 다음으로 `private static inner class` 에 대해서 같이 생각해보면 좋을 것 같다.
  - 즉 inner class는 왜 주로 private static를 선언하는가?
  - ex) Map의 구현 클래스 Entry의 객체