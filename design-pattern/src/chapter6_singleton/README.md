# 싱글톤 패턴
- 최초 한번만 메모리에 할당하여 생성하여 인스턴스가 **오직 단 하나만 생성되는 것**을 보장하고, **어디에서든 이 인스턴스에 접근**할 수 있는 패턴

<br>

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
            synchronized (this) {
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
- 첫번째 if 문에서 null임이 확인된 이후에는 synchronized 블럭으로 넘어가게 되는데, 이 때는 이 블럭이 단 하나의 쓰레드만 내부로 접근할 수 있도록 막는다.
- 동기화를 해버리면 성능 저하 이슈를 고려해야한다.
- 생성하는 부분과 상태값이 변경되는 부분에 동기화를 신경써줘야 한다.

4. LazyHolder
- 동기화가 필요 없는 방법.
- inner class를 이용해서 inner class를 참조하는 순간 Class가 로딩되어 초기화가 진행된다.
- inner class 안에 선언된 instance가 static이기 때문에 클래스 로딩 시점에 한번만 호출된다.
- JVM 클래스 초기화 과정에서 thread-safe 하고 final 키워드를 사용해 다시 값을 할당하지 않도록 한다.

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
