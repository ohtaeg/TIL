# 컴포지트 패턴
- 부분 - 전체 의 관계를 갖는 객체들을 정의할 때 유용한 패턴
  - Part - Whole
- 클라이언트는 전체와 부분을 구분하지 않고 동일한 인터페이스를 사용할 수 있다.

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter14_composite/uml/composite.puml)

- Component : 전체 - 부분 관계 중에 `부분`에 해당하는 인터페이스, Composite 와 구체 Component에 해당하는 공통 인터페이스이다.
  - 클라이언트가 전체나 부분을 모두 동일한 컴포넌트로 인식할 수 있는 구조로 만들 수 있다. 
- Composite : 전체 - 부분 관계 중에 `전체`에 해당하는 클래스, 복수개의 Component를 가질 수 있다.
  - Component 가 지원하는 기능을 Composite 에서도 지원해야한다.
- Leaf : Concrete Component, 컴포넌트 인터페이스의 구현체, 기본적인 단위

### 장단점
- 장점
  - 복잡한 트리 구조를 편리하게 사용할 수 있다.
  - 다형성과 재귀를 활용할 수 있다.
  - 클라이언트 코드를 변경하지않고 타입을 추가할 수 있다.
    - 새로운 타입의 Leaf나 새로운 Composite가 추가되더라도 클라이언트 코드의 변경은 없다.
- 단점
  - 트리를 만들어야하기 때문에 (공통된 인터페이스를 정의해야하기 때문에) 억지로 추상화 해야하는 경우가 생길 수 있다.
    - 어울리지 않는 특정한 디자인 패턴에 종속되어 개발할 수도 있다.


### Composite patterns in Java
- javax.swing.JFrame
  - Component -> Component
  - JFrame -> Composite
  - JTextField, JButton -> Leaf
### Composite patterns in Spring
  
