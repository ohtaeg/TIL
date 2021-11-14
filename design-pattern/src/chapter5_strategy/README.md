# Chapter 5. 전략 패턴
- 전략이란
    - 어떤 목적을 달성하기 위해 일을 수행하는 방식
    - 비즈니스 규칙
    - 문제를 해결하는 알고리즘
- 기존 코드에 새로운 기능으로 변경하려고 한다면, 보통 기존 코드의 내용을 수정해야 하므로 OCP에 위배된다.
- 해당 문제를 해결하려면 **`무엇이 변화하는지 찾아야한다.`**
- **`변화된 것을 찾은 후에 이를 클래스로 캡슐화 한다.`**
- **`캡슐화 후에 구체적인 클래스들을 은닉해야한다.`**

<br>

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter5_strategy/uml/strategy.puml)

- Context : 스트래티지 패턴을 사용하는 녀셕, 필요에 따라 구체적인 전략을 동적으로 바꿀 수 있는 메서드 제공해야한다.
- Strategy : 인터페이스나 추상 클래스로 외부에서 동일한 방식으로 알고리즘을 호출하는 방법을 명시
- ConcretStrategies : 실제로 구현된 전략 객체들

