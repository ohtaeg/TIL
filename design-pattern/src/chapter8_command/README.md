# 커맨드 패턴
- **요청을 호출하는 클래스**와 **요청을 수신해서 처리하는 클래스**간의 요청(커맨드)라는 인터페이스를 중간에 두어 의존성을 약화시키려는 패턴
  - **요청(실행될 기능)을 캡슐화하여 호출자(Invoker) 와 수신자(Receiver)를 분리**
- 이벤트를 발생시키는 클래스를 변경하지 않고 재사용하고자 할 때 유용하다.
- **`요청을 처리하는 방법이 바뀌더라도, 호출자의 코드는 변경되지 않는다.`**
  - Invoker 코드가 안바뀌는것이 중요하다. 호출하는 측은 클라이언트, 고객이거나 외부라고 말할 수 있다.
- Receiver가 누구이고 Receiver의 어떤 메서드를 실행해야하는지, Receiver를 호출할 때 필요한 파라미터는 무엇인지 모든 작업들을 Command라는 인터페이스 안으로 캡슐화

<br>

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter8_command/uml/command.puml)

<br>

- Command : 실행될 기능에 대한 인터페이스, execute() 메서드로 구현한다.
- ConcreteCommand : 실제로 실행되는 기능을 구현
- Invoker : 기능 실행을 요청하는 호출자, **`Command를 호출하는 클래스`**, 보통 execute() 메서드 하나를 명시한다. 경우에 따라서 undo() 메서드도 명시한다.
  - invoke : 지정된 매개 변수를 사용하여 해당 인스턴스의 메서드 또는 생성자를 호출
- Receiver : ConcreteCommand 에서 execute() 메서드를 구현할 때 필요한 **`커맨드(이벤트)의 기능을 수행`**하는 클래스


<br>
<br>

![class-diagram2](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter8_command/uml/button.puml)

<br>

- Button 클래스는 Invoker 역할인 기능 수행을 요청한다.
- Lamp 클래스는 Receiver 역할인 커맨드(이벤트)의 기능을 실행하는 수신자 클래스이다.

<br>

### 장단점
- 장점
  - Invoker, Receiver 기존의 코드를 크게 변경하지 않는다. OCP 준수
  - Command 각 구현 객체가 자기 할 일만 한다. SRP 준수
  - Command를 다양하게 활용할 수 있다.
    - undo()
- 단점
  - 코드가 복잡해질 수 있다.