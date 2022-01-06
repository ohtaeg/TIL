# 팩토리 메서드 패턴

- 객체의 생성과 관련된 패턴
- 어떤 인스턴스를 생성하는 책임을 구체적인 클래스가 아니라 추상적인 인터페이스의 메서드로 감싸는 패턴
- 어떤 인스턴스를 만들지는 Factory 서브 클래스가 정한다.
- 다양한 구현체 (Product)가 있을 때, 특정한 구현체를 만들 수 있는 다양한 팩토리(Creator)를 제공할 수 있다.

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter12_factory-method/uml/factory-method.puml)

### 팩토리 메서드 패턴 장단점

- 장점 
  - 외부(Client)로부터의 `구현체의 생성 책임을 방지`할 수 있다. 
    - 즉, 객체를 사용하는 측에서 구체적인 타입의 존재를 모르도록 `타입 은닉`을 할 수 있다.
  - 필요할 때 만들어 써서 메모리 효율을 높일 수 있다. (쓸때없는 객체 생성을 방지할 수 있다.)
  - 객체 생성 후 템플릿 메서드패턴 처럼 공통으로 작업을 수행할 수 있게 해준다.
    - 객체가 이벤트를 수신하는 경우, 만약 객체를 이벤트 수신자로 등록하는 코드를 생성자 내부에 넣어 두게 되면 <br>
    생성이 완료 되기 이전에 이벤트를 받게 되어 오류가 발생하는 경우가 생긴다. 이런 일들은 객체 생성 이후에 해주어야 하는데, Factory Method 패턴을 이용하면 이 문제를 해결할 수 있다.
- 단점
  - Factory Method 패턴에서 구체 Factory들이 여러번 생성될 필요가 없도록 할 수 있는데 이 과정에서 Singleton 패턴을 이용한다면 패턴이 복잡해지는 문제점이 있을 수 있다
  - Enum Factory Method를 구현함으로써 해결할 수 있다.
  ```java
  public enum EnumFactoryMethod {
  
      RECTANGLE {
          protected Shape createShape(){return new Rectangle();}
      }
      , CIRCLE {
          protected Shape createShape(){return new Circle();}
      }
      ;
  
      public Shape create(Color color){
          Shape shape = createShape();
          shape.setColor(color);
          return shape;
      }

      abstract protected Shape createShape();
  
      public static void main(String[] args) {
          EnumFactoryMethod.RECTANGLE.create(Color.BLACK);
          EnumFactoryMethod.CIRCLE.create(Color.WHITE);
      }
  }
  ```

<br>

### Factory Method patterns in Java
- java.util.Calendar
- java.lang.NumberFormat

### Factory Method patterns in Spring
- BeanFactory
```java
class Client {
  public static void main(String[] args) {
    BeanFactory xmlFactory = new ClassPathXmlApplicationContext("config.xml");
    xmlFactory.getBean("defaultXmlFactory", String.class);
    
    BeanFactory javaFactory = new AnnotationConfigApplicationContext(Config.class);
    javaFactory.getBean("hello", String.class);
  }    
}
```