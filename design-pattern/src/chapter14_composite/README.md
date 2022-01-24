# 컴포지트 패턴
- 부분 - 전체 의 관계를 갖는 객체들을 정의할 때 유용한 패턴
  - Part - Whole
- 클라이언트는 전체와 부분을 구분하지 않고 동일한 인터페이스를 사용할 수 있다.

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter14_composite/uml/composite.puml)

- Composite : 전체 - 부분 관계 중에 전체에 해당하는 클래스, 복수개의 Component를 가질 수 있다.
- Component : 전체 - 부분 관계 중에 부분에 해당하는 인터페이스, Composite 와 구체적은 Component에 해당하는 공통 계약을 정의한다.
- Leaf : Concrete Component, 컴포넌트 인터페이스의 구현체
