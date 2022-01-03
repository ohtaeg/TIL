# 템플릿 메서드 패턴
- 알고리즘 구조를 서브 클래스가 확장할 수 있도록 템플릿으로 제공하는 패턴
- 알고리즘 구조를 추상 클래스 템플릿으로 제공하고, 구체적인 방법 등은 템플릿을 상속받는 Sub Class가 구현하는 패턴
- 

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter11_template/uml/template.puml)
- AbstractClass : 템플릿 메서드를 정의하는 클래스, 하위 클래스에 공통적인 알고리즘을 정의
- ConcreteClass : 물려받은 템플릿 메서드(hook)를 구현하는 클래스


<br>

#### 장단점
- 장점
  - 템플릿 코드 재사용을 함으로써 중복 코드를 줄일 수 있다.
  - 템플릿 코드를 변경하지 않고 상속을 받아 구체적인 로직만 변경할 수 있다.
- 단점
  - LSP 원칙을 위반할 수 있다.
    - 이 단점은 템플릿 메서드 패턴의 단점이라기보다는 보통 상속의 문제라고도 볼 수 있는데, 하위 클래스에서 메서드를 재정의할시 의도에 어긋나게 재정의할 가능성이 충분히 높기 때문이라고 생각함.
  - 알고리즘 구조가 복잡할 수록 템플릿 코드가 복잡해질 수 있다.
  
<br>

## 템플릿 콜백 패턴
- 콜백으로 상속 대신 위임을 사용하는 템플릿 패턴
  - 콜백을 사용함으로써 상속 대신 위임을 사용할 수 있다.
  - 스프링에서 많이 쓰이는 패턴이다.
- 상속 대신 익명 내부 클래스 or 람다 표현식을 활용한다.
  - 콜백 인터페이스가 전략 패턴과 비슷해보인다.
  - 전략 패턴은 여러 메서드를 추상화할 수 있지만, Callback 인터페이스는 오직 하나의 메서드만 추상화한다.
    - 여러 메서드를 추상화해야할 경우 여러 콜백 인터페이스를 만든다.

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ohtaeg/TIL/master/design-pattern/src/chapter11_template/uml/template_callback.puml)

<br>

---

### Template pattern in java
- All non-abstract methods of java.util.AbstractList, java.util.AbstractSet and java.util.AbstractMap.
- [javax.servlet.http.HttpServlet](https://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServlet.html)

### Template pattern in spring
- WebSecurityConfigureAdapter

### Template Callback pattern in spring
- org.springframework.jdbc.core.JdbcTemplate
```java
public class JdbcTemplate extends JdbcAccessor implements JdbcOperations {
    
  @Nullable
  public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
    Assert.notNull(action, "Callback object must not be null");
    Connection con = DataSourceUtils.getConnection(this.obtainDataSource());

    Object var10;
    try {
      Connection conToUse = this.createConnectionProxy(con);
      var10 = action.doInConnection(conToUse);
    } catch (SQLException var8) {
      String sql = getSql(action);
      DataSourceUtils.releaseConnection(con, this.getDataSource());
      con = null;
      throw this.translateException("ConnectionCallback", sql, var8);
    } finally {
      DataSourceUtils.releaseConnection(con, this.getDataSource());
    }

    return var10;
  }


  @Nullable
  public <T> T query(final String sql, final ResultSetExtractor<T> rse) throws DataAccessException {
    Assert.notNull(sql, "SQL must not be null");
    Assert.notNull(rse, "ResultSetExtractor must not be null");
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Executing SQL query [" + sql + "]");
    }

    class QueryStatementCallback implements StatementCallback<T>, SqlProvider {
      QueryStatementCallback() {
      }

      @Nullable
      public T doInStatement(Statement stmt) throws SQLException {
        ResultSet rs = null;

        Object var3;
        try {
          rs = stmt.executeQuery(sql);
          var3 = rse.extractData(rs);
        } finally {
          JdbcUtils.closeResultSet(rs);
        }

        return var3;
      }

      public String getSql() {
        return sql;
      }
    }

    return this.execute((StatementCallback)(new QueryStatementCallback()));
  }
  
}
```
- org.springframework.web.client.RestTemplate
```java
public class RestTemplate extends InterceptingHttpAccessor implements RestOperations {
    
  @Nullable
  protected <T> T doExecute(URI url, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {
    Assert.notNull(url, "URI is required");
    Assert.notNull(method, "HttpMethod is required");
    ClientHttpResponse response = null;

    Object var14;
    try {
      ClientHttpRequest request = this.createRequest(url, method);
      
      // here
      if (requestCallback != null) {
        requestCallback.doWithRequest(request);
      }

      response = request.execute();
      this.handleResponse(url, method, response);
      var14 = responseExtractor != null ? responseExtractor.extractData(response) : null;
    } catch (IOException var12) {
      String resource = url.toString();
      String query = url.getRawQuery();
      resource = query != null ? resource.substring(0, resource.indexOf(63)) : resource;
      throw new ResourceAccessException("I/O error on " + method.name() + " request for \"" + resource + "\": " + var12.getMessage(), var12);
    } finally {
      if (response != null) {
        response.close();
      }

    }

    return var14;
  }
  
}
```

