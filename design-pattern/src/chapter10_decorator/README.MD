# 데코레이터 패턴
- 기존 코드를 변경하지 않으면서 부가적인 기능을 동적으로 추가할 수 있는 패턴
- 장점 
  - 기능 조합 별로 경우의 수에 맞게 클래스를 생성하지 않아도 된다.
- 단점
  - 복잡도가 높아질 수 있다.

### 문제점

- 네비에 도로 표시하는 기능을 제공하는 SW를 만든다고 할 때,
- 기본 도로 표시 뿐만 아니라 여러 정보를 추가로 함께 보여주고 싶을 때 **기능 조합 별로 경우의 수에 맞게 구현체들을 만들어야 하는 단점**이 있다.
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

- ConcreteComponent : 기본 기능을 구현한 클래스, 완제품으로 객체를 생성하는데 다른 객체를 필요로 하지않고, 포함하지 않는다.
- Component : 외부에 노출된 인터페이스, 기본 기능인 ConcreteComponent 와 추가 기능인 Decorator의 공통 기능을 정의
- Decorator : 많은 추가기능에 대한 Decorator의 공통 기능 제공
- ConcreteDecorator : 추가 기능을 구현한 Decorator
- Decorator 와 ConcreteComponent가 같은 Component 타입 계층으로써 유연성 확보

---

### Composite 패턴과의 차이점
- 컴포지트 패턴과의 큰 차이점은 데코레이터의 갯수이다.
- 컴포지트 패턴은 여러개의 Decorator를 가질 수 있다.
- 데코레이터 패턴은 하나의 Decorator 인스턴스만 가진다.

---

### Decorator pattern in java
- All subclasses of java.io.InputStream, OutputStream, Reader and Writer have a constructor taking an instance of same type.
  - `BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); `
  - `DataInputStream dis = new DataInputStream(new FileInputStream(""));`
  - ![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter10_decorator/uml/inputstream.puml)
  - http://stg-tud.github.io/sedc/Lecture/ws13-14/5.3-Decorator.html#mode=document
- java.util.Collections, the checkedXXX(), synchronizedXXX() and unmodifiableXXX() methods.
- [javax.servlet.http.HttpServletRequestWrapper and HttpServletResponseWrapper](https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequestWrapper.html)

### Decorator pattern in spring
- [CGLIB proxying](https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch09s05.html#aop-global-advisors)
- [BeanDefinitionDecorator](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/xml/BeanDefinitionDecorator.html)
  - 빈 설정 데코레이터
- [WebSocketHandlerDecorator](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/socket/handler/WebSocketHandlerDecorator.html)
  - 스프링 리엑티브 API
- [ServerHttpRequestDecorator](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/server/reactive/ServerHttpRequestDecorator.html)
  - 웹플럭스 HTTP 요청 / 응답 데코레이터
  


