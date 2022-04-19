# 상태(State) 패턴
- 상태란 객체가 시스템에 존재하는 동안 (라이프 타임) 객체가 가질 수 있는 조건이나 상황을 나타낸 것 
- 객체 내부 상태 변경에 따라 객체의 행동이 달라지는 패턴
- 상태에 특화된 행동들을 분리해 낼 수 있으며, 새로운 행동을 추가하더라도 다른 행동에 영향을 주지 않는다.
  - 변하는 부분을 캡슐화 한다. => 상태를 클래스로 분리해 캡슐화 한다.
  - 상태에 의존적인 행동들도 상태 클래스에서 구현한다.
  - **상태 변경은 상태 스스로 알아서 다음 상태를 결정한다.** (경우에 따라 상태 변경을 관리하는 클래스가 있을 수 있다.)
- 예로 블로그에 글을 쓰는데, 처음 쓸 때는 DRAFT(초안) 상태라서 사람들이 볼 수 없고, 댓글 남기는 것도 불가능
- 만약 글이 공개가 되면 다른 사람들이 댓글도 달고 좋아요도 누르고 할 수 있게 된다.

<br>

![sequence-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter7_state/uml/state.puml)

<br>

- State : 변경될 수 있는 여러 상태에 대한 공통된 인터페이스
- Context : State를 이용하는 객체, 상태에 따라 행동을 다르게 해야하기 때문에 상태 변경을 관리한다.
  - Context가 행동에 따라 달라지는 opertion을 State 인터페이스를 통해 ConcreteContext 에게 위임을 한다.
- ConcreteContext : 특정 상태 변화에 따른 행동이 정의된 객체