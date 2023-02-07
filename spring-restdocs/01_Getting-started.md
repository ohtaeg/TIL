# Spring REST Docs
- 업무를 진행하면서 단순 문서화가 아닌 OAS 추출 등 추가 작업을 진행하다보니 사용법만 익히고 제대로 본적이 없다.
- 이번 기회에 [문서](https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/)를 읽으면서 정리해나가자. 필요한 부분만 발췌해서 정리한다.
- 현재 REST Docs의 버전은 다음과 같다.
  - `Version 3.0.0.RELEASE`
- 전체적으로 훑어볼 목차는 다음과 같다.
  - Getting-started
  - Documenting your API
  - Customizing requests and responses
  - Configuration
  - Working with Asciidoctor

## Introduce
```text
Spring REST Docs uses Asciidoctor by default.
Asciidoctor processes plain text and produces HTML, styled and laid out to suit your needs.
If you prefer, you can also configure Spring REST Docs to use Markdown.
```
- 스프링 REST Docs는 기본적으로 `Asciidoctor`를 사용한다.
- `Asciidoctor`는 텍스트를 처리해서 필요에 맞는 HTML 형식으로 만들고 원하면 Markdown을 사용하도록 설정할 수 있다.
- REST Docs를 통해 .adoc 파일이 만들어지는데 `Asciidoctor`를 통해 HTML로 추출할 수 있다는 뜻

### Asciidoctor란?
```
Asciidoctor is a fast, open source, Ruby-based text processor for parsing AsciiDoc® into a document model 
and converting it to output formats such as HTML 5, DocBook 5, manual pages, PDF, EPUB 3, and other formats.
```
- [Acidoctor](https://asciidoctor.org/)는 ASCIIDoc를 문서 모델로하는 Ruby 기반의 텍스트 프로세서이다.
- HTML 5, DocBook 5, PDF, 등 출력 형식으로 변환이 가능하다.
- 즉 ASCIIDoc 기반의 문서를 HTML 등의 문서로 변환이 가능하게 해주는 변환기라고 해석이 된다.


<br>
<br>
<br>

## Requirements
- 최소 환경은 다음과 같다.
  - JAVA 8
  - SPRING 5.0.2 OR later

## Build configuration
- gradle
```groovy
plugins { // (1)
	id "org.asciidoctor.convert" version "1.5.9.2"
}

dependencies {
	asciidoctor 'org.springframework.restdocs:spring-restdocs-asciidoctor:{project-version}' // (2)
	testCompile 'org.springframework.restdocs:spring-restdocs-mockmvc:{project-version}' // (3)
}

ext { // (4)
	snippetsDir = file('build/generated-snippets')
}

test { // (5)
	outputs.dir snippetsDir
}

asciidoctor { // (6)
	inputs.dir snippetsDir // (7)
	dependsOn test // (8)
}
```
- (1)
  - asciidoctor 플러그인을 적용
  - 즉 ASCIIDoc를 html 등으로 변환하기 위한 변환기를 적용한다는 뜻
- (2)
  ```text
  Add a dependency on spring-restdocs-asciidoctor in the asciidoctor configuration. 
  This will automatically configure the snippets attribute for use in your .adoc files to point to build/generated-snippets.
  It will also allow you to use the operation block macro.
  ```
  - asciidoctor 의존성을 추가한다. **spring-restdocs-asciidoctor**
  - **build/generated-snippets** 에 `.adoc` 파일에 사용되어지는 `snippets` 이 자동으로 설정된다.
- (3)
  - testCompile에 spring-restdocs-mockmvc 의존성 추가
  - REST Assured를 사용하고 싶다면 spring-restdocs-restassured 의존성 추가
- (4)
  - 생성된 `snippets`의 결과물 파일의 위치
- (5)
  - test 테스크에 `snippets` 디렉토리를 추가
  - (4)번에 명시한 위치에 gradle task 중 test가 실행되면 해당 위치로 `snippets` 생성한다는 뜻
- (6)
  - asciidoctor 테스크를 설정
- (7)
  - 스니펫 디렉토리를 입력으로 설정
- (8)
  - asciidoctor 테스크를 test 테스크에 의존하도록 설정, 문서 생성 전에 테스트를 실행하도록 설정

<br>

## Packaging the Documentation
- 생성한 문서를 jar 파일과 함께 패키징하고 싶은 경우 다음과 같이 설정할 수 있다.
- gradle
```groovy
bootJar {
	dependsOn asciidoctor // (1)
	from ("${asciidoctor.outputDir}/html5") {  // (2)
		into 'static/docs'
	}
}
```
- (1)
  - asciidoctor 테스크를 bootJar 테스크에 의존하도록 설정, 문서를 생성하고 bootJar 테스크를 수행한다.
- (2)
  - 생성한 문서를 static/docs 디렉토리로 이동시킨다.

<br>
<br>
<br>

## Generating Documentation Snippets
- 위에 설정에서 `snippets`을 언급했었는데 해당 키워드는 다음과 같다. 
  - .adoc 파일에 사용되어지는 snippets
  - 조각이란 뜻으로 스니펫이 모여서 문서가 구성된다.
```text
Spring REST Docs uses Spring MVC’s test framework, Spring WebFlux’s WebTestClient, or REST Assured to make requests to the service that you are documenting.
It then produces documentation snippets for the request and the resulting response.
```
- 스프링 REST Docs는 Webclient, REST Assured 등을 통해 문서화를 진행하고, 응답 결과로 문서 스니펫을 만든다.

<br>

### Setting up Your JUnit5 Tests
- Junit5로 스니펫을 만들려면 테스트 클래스에 `RestDocumentationExtension`을 적용한다.
```java
@ExtendWith(RestDocumentationExtension.class)
public class JUnit5ExampleTests {
}
```
- 스프링 어플리케이션을 테스트할 땐 `SpringExtension`도 같이 적용해준다.
```java
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class JUnit5ExampleTests {
}
```
- `RestDocumentationExtension` 이녀석은 빌드 툴 기반으로 **출력 디렉토리를 설정**한다.
  - gradle : `build/generated-snippets`

<br>

```text
If you are using JUnit 5.1, you can override the default by registering the extension as a field in your test class and providing an output directory when creating it.
The following example shows how to do so:
```
- JUnit 5.1을 사용한다면 위에서 설정했던 **클래스 어노테이션 방식을 필드 어노테이션 방식으로 설정**할 수 있다.
- 필드로 제공한다면 **출력 디렉토리 디폴트 설정을 재정의**할 수 있다.
```java
public class JUnit5ExampleTests {
	@RegisterExtension
	final RestDocumentationExtension restDocumentation = new RestDocumentationExtension ("custom");
}
```
- 그다음 @BeforeEach 메서드에서 설정하는 방법은 다음과 같다. (REST Assured)
```java
public class JUnit5ExampleTests {
  // ...
  private RequestSpecification spec;

  @BeforeEach
  public void setUp(RestDocumentationContextProvider restDocumentation) {
    this.spec = new RequestSpecBuilder()
            .addFilter(documentationConfiguration(restDocumentation))
            .build();
  }
}
```
- RestAssuredRestDocumentationConfigurer를 Filter로 추가해줘야 한다.
- org.springframework.restdocs.restassured3 패키지에 있는 RestAssuredRestDocumentation의 스태틱 메소드인 documentationConfiguration
  ()을 통해 가져올 수 있다.

<br>

## Invoking the RESTful Service
- 위에서 테스트 프레임워크 설정을 완료했으며, RESTfus 서비스에 대해 요청과 응답에 대한 문서화를 알아보자.
```text
RestAssured.given(this.spec)    (1) 
		.accept("application/json") (2) 
		.filter(document("index"))  (3) 
		.when().get("/")    (4)
		.then().assertThat().statusCode(is(200));   (5) 
```
1. @BeforeEach에서 초기화한 스펙을 적용
2. application/json 응답이라고 적용
3. "index"라는 값으로 된 디렉토리(**설정한 출력 디렉토리**)에 스니펫을 작성한다. 스니펫은 RestDocumentationFilter가 작성한다.
   - org.springframework.restdocs.restassured3 패키지의 RestAssuredRestDocumentation.document()를 호출하여 필터를 가져올 수 있다.
4. 해당 서비스 경로의 GET 메소드 호출
5. 결과가 200인지 검증

- 위 코드는 아래와 같이 기본적으로 여섯 가지 스니펫을 작성한다.
  - <output-directory>/index/curl-request.adoc
  - <output-directory>/index/http-request.adoc
  - <output-directory>/index/http-response.adoc
  - <output-directory>/index/httpie-request.adoc
  - <output-directory>/index/request-body.adoc
  - <output-directory>/index/response-body.adoc

<br>

## Using the Snippets
- 생성된 스니펫을 사용하려면 .adoc 파일을 개발자가 직접 만들어야한다.
  - 이름은 상관없다.
- 만들어지는 HTML 파일도 같은 이름으로 생성된다.
- gradle를 사용하면 생성된 HTML 파일의 경로는 다음과 같다.
  - adoc : src/docs/asciidoc/*.adoc
  - html : build/asciidoc/html5/*.html
- 만들어진 스니펫은 include 키워드를 사용해 수동으로 Asciidoc 파일에 넣을 수 있다.
- 아래 속성을 활용하면 되는데, 이 속성을 활용하면 asciidoctor을 설정한 경우 자동으로 스니펫을 추가해준다.
  - include::{snippets}/index/curl-request.adoc[]