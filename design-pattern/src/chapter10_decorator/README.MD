# 데코레이터 패턴
- 기존 코드를 변경하지 않으면서 부가적인 기능을 동적으로 추가할 수 있는 패턴


### 문제점

- 네비에 도로 표시하는 기능을 제공하는 SW를 만든다고 할 때,
- 기본 도로 표시 뿐만 아니라 여러 정보를 추가로 함께 보여주고 싶을 때 **기능 조합 별로 구현체들을 만들어야 하는 단점**이 있다.
    - 특히나 다양한 기능의 조합을 고려한 경우 상속을 통한 기능 확장은 더더욱 문제

```java
public class RoadDisplay {
    void draw() {
        System.out.println("도로 표시");
    }
}

// 도로에 차선 표시
public class RoadDisplayWithLane extends RoadDisplay {
    @Override
    public void draw() {
        super.draw();
        drawLane();
    }
    
    private void drawLane() {
        System.out.println("차선 표시");
    }
}

// 차선과 교통랑 표시
public class RoadDisplayWithLaneTraffic extends RoadDisplay {

  @Override
  void draw() {
    super.draw();
    drawLane();
    drawTraffic();
  }

  private void drawLane() {
    System.out.println("차선 표시");
  }

  private void drawTraffic() {
    System.out.println("교통랑 표시");
  }
}
```

<br>

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter10_decorator/uml/before.puml)

<br>

---

### 해결책
- 추가 기능마다 개별 클래스로 만들고, 상속이 아닌 `조합`을 이용한다.
- 기본 기능에 추가할 수 있는 기능이 많은 경우 추가 기능을 Decorator 클래스로 정의한 후 필요한 Decorator 클래스를 조합하여 설계

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter10_decorator/uml/after.puml)

<br>

----

### UML

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter10_decorator/uml/decorator.puml)

- ConcreteComponent : 기본 기능을 구현한 클래스
- Component : 기본 기능인 ConcreteComponent 와 추가 기능인 Decorator의 공통 기능을 정의
- Decorator : 많은 추가기능에 대한 Decorator의 공통 기능 제공
- ConcreteDecorator : 추가 기능을 구현한 Decorator


