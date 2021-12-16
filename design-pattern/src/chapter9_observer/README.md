# 옵저버 패턴
- 상태의 변경을 감시하는 패턴
  - 데이터의 변경이 발생했을 경우, 이에 관심을 가지는 모든 대상 객체들에게 의존하지 않고 데이터 변경을 통보하고자 할 때 유용하다.
  - **데이터 변경을 통보하는 클래스 (ConcreteSubject)는 통보 대상 클래스 (ConcreteObserver)를 수정없이 사용할 수 있도록 의존성을 없앨 수 있다.**
- ex)
  - 새로운 파일이 추가되거나 기존 파일이 삭제 되었을 때 탐색기에 표시할 경우
  - 차량의 연료가 소진될 때까지의 주행 가능 거리를 출력
  - 연료가 부족하면 경고 메시지를 보내는 기능


<br>

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter9_observer/uml/observer.puml)

<br>

![sequence-diagram]()

- Observer : 데이터의 변경을 통보 받는 인터페이스, Subject에서 Observer 인터페이스의 update() 메서드를 호출함으로써, ConcreteSubject의 데이터 변경을 ConcreteObserver에게 통보한다.
- ConcreteObserver : ConcreteSubject의 변경을 통보받는 클래스, ConcreateSubject를 참조하여 변경된 데이터는 ConcreteSubject의 getState() 메서드를 호출함으로써 변경을 조회한다. 
- Subject : Observer 인터페이스를 참조해서 ConcreteObserver 객체를 관리하는 클래스
- ConcreteSubject : 변경 관리 대상 객체, 데이터가 변경이 되면 Subject 클래스의 notifyObservers 메서드를 호출해 통보 대상 객체들 (ConcreteObserver)에게 데이터의 변경을 통보한다.

