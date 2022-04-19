# 옵저버 패턴
- 다수의 객체가 특정 객체 상태의 변경을 감지하고 알림을 받는 패턴
- 발행 (publish) - 구독 (subscribe)
  - 데이터의 변경이 발생했을 경우, 이에 관심을 가지는 모든 대상 객체들에게 의존하지 않고 데이터 변경을 통보하고자 할 때 유용하다.
  - **데이터 변경을 통보하는 클래스 (ConcreteSubject)는 통보 대상 클래스 (ConcreteObserver)를 수정없이 사용할 수 있도록 의존성을 없앨 수 있다.**
- ex)
  - 새로운 파일이 추가되거나 기존 파일이 삭제 되었을 때 탐색기에 표시할 경우
  - 차량의 연료가 소진될 때까지의 주행 가능 거리를 출력
  - 연료가 부족하면 경고 메시지를 보내는 기능


- 옵저버들은 상태를 꼭 갖고 있을 필요는 없다.
  - 상태는 Subject가 담당하므로 Observer가 굳이 상태를 저장할 필요가 없을 경우 저장하지 않아도 된다.

<br>

----
## UML

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter9_observer/uml/observer.puml)

<br>

- Observer : 데이터의 변경을 통보 받는 인터페이스, Subject에서 Observer 인터페이스의 update() 메서드를 호출함으로써, ConcreteSubject의 데이터 변경을 ConcreteObserver에게 통보한다.
- ConcreteObserver : ConcreteSubject의 변경을 통보받는 클래스, ConcreateSubject를 참조하여 변경된 데이터는 ConcreteSubject의 getState() 메서드를 호출함으로써 변경을 조회한다. 
- Subject : 여러 Observer 들을 등록하고 해지, Observer 인터페이스를 참조해서 ConcreteObserver 객체를 관리하는 클래스
- ConcreteSubject : 변경 관리 대상 객체, 데이터가 변경이 되면 Subject 클래스의 notifyObservers 메서드를 호출해 통보 대상 객체들 (ConcreteObserver)에게 데이터의 변경을 통보한다.

<br>

----

### 장단점
- 장점
  - 상태를 변경하는 publisher 객체와 변경을 감지하는 subscriber 객체간의 관계를 느슨하게 할 수 있다.
  - Subject의 상태 변경을 주기적으로 조회하지 않고 자동으로 감지할 수 있다.
  - 런타임시에 옵저버를 추가하거나 제거할 수 있다.
- 단점
  - 복잡도가 증가한다.
  - 통보 체인이 걸릴 수 있다. 한 Observer가 통보를 받았을 때 자신이 다시 주체가 되어 다른 Observer에게 통보하는 식
    - 이런 연속적인 통보가 발생한다면 누가 누굴 왜 통보했는지 디버깅도 어렵고 설계가 어려워진다.
  - 다수의 Observer를 등록하고 나중에 해지하지 않으면 memory leak이 발생할 수도 있다.

<br>

----

### Java 에서의 Observer
- Observer (interface), Observable (class) 을 제공해주는데 1.9부터 Deprecated 되었다.
- [문서](https://docs.oracle.com/javase/9/docs/api/java/util/Observable.html) 를 잠깐 읽어보면

```
This class and the Observer interface have been deprecated. 
The event model supported by Observer and Observable is quite limited,
the order of notifications delivered by Observable is unspecified,
and state changes are not in one-for-one correspondence with notifications. 

For a richer event model, consider using the java.beans package. 
For reliable and ordered messaging among threads, consider using one of the concurrent data structures in the java.util.concurrent package. 
For reactive streams style programming, see the Flow API.
```

1. Observer and Observable이 지원하는 event 모델은 매우 제한적이다.
2. Observable의 알림은 구체적인 순서를 보장하지 않는다.
3. 상태 변경은 알림과 1:1 대응되지 않는다.
4. 더 풍부한 이벤트 모델은 java.beans package 에서 제공하고 있다.
   - java.beans.PropertyChangeListener & PropertyChangeSupport 
5. 스레드 간 순서가 보장된 믿을 만한 메시지 내용은 java.util.concurrent 패키지에서 concurrent 자료구조 하나를 사용하는 것이 좋다.
6. reactive streams 스타일 프로그래밍은 Flow Api를 사용해라.
7. setChanged() 메서드를 호출해야만 알림을 보낼 수 있다.
8. 여러번 알림을 하기 위해서 다시 setChanged()를 호출해야한다.
    ```
    public void notifyObservers(Object arg) {
            Object[] arrLocal;
    
            synchronized (this) {
                if (!changed)
                    return;
                arrLocal = obs.toArray();
                clearChanged(); // 여기서 false로 바꿔버린다.
            }
            
            ...
        }
    }
    ```