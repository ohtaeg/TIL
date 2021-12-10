# 커맨드 패턴
- 이벤트를 발생시키는 클래스를 변경하지 않고 재사용하고자 할 때 유용하다.
- 실행될 기능을 캡슐화함으로써 기능 실행을 요구하는 호출자(Invoker) 와 기능을 실행하는 수신자(Receiver) 사이의 의존성을 제거한다.

<br>

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter8_command/uml/command.puml)

<br>

- Command : 실행될 기능에 대한 인터페이스, execute() 메서드로 구현한다.
- ConcreteCommand : 실제로 실행되는 기능을 구현
- Invoker : 기능 실행을 요청하는 호출자
  - invoke : 지정된 매개 변수를 사용하여 해당 인스턴스의 메서드 또는 생성자를 호출
- Receiver : ConcreteCommand 에서 execute() 메서드를 구현할 때 필요한 클래스

<br>
<br>

![class-diagram2](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter8_command/uml/button.puml)

<br>

- Button 클래스는 Invoker 역할인 기능 수행을 요청한다.
- Lamp 클래스는 Receiver 역할인 커맨드(이벤트)의 기능을 실행하는 수신자 클래스이다.