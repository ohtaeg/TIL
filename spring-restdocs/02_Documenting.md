# Documenting your API
- 해당 목차는 [공식 문서](https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/#documenting-your-api)에서 REST Docs로 API를 문서화 하는 방법을 읽고 정리한다.

## Hypermedia
- 하이퍼미디어 기반 API에서 링크도 문서화를 할 수 있다.
```text
RestAssured.given(this.spec)
	.accept("application/json")
	.filter(document("index", links( // (1) 
			linkWithRel("alpha").description("Link to the alpha resource"),   // (2) 
			linkWithRel("bravo").description("Link to the bravo resource")))) // (3)
	.get("/").then().assertThat().statusCode(is(200));
```
- (1)
  - 응답에 있는 링크들을 설명하는 스니펫을 만들도록 설정
  - org.springframework.restdocs.hypermedia.HypermediaDocumentation.links()를 이용
- (2)
  - "alpha" 링크가 있는지 검사한다.
- (3)
  - "bravo" 링크가 있는지 검사한다.

- 위 코드의 결과로 links.adoc 이란 스니펫을 만든다.
- 링크를 문서화할 때 응답에 있는 모든 링크를 작성하지 않으면 테스트는 실패한다.
- 링크를 문서화하고 싶지 않다면 마킹을하여 무시할 수 있다. 이때 테스트 실패를 방지하고 스니펫에도 제외시킨다.
- 모든 링크를 문서화하지 않고 일부 링크만 문서화하려면 relaxedLinks()를 이용한다.

<br>

### Hypermedia Link Formats
- 생략, 필요할 때 정리

### Ignoring Common Links
- 생략, 필요할 때 정리

<br>
<br>

## Request and Response Payloads
- 기본적으로 스프링 REST Docs는 요청 바디와 응답 바디의 스니펫을 자동으로 만들어준다.
- 각 스니펫 이름은 **request-body.adoc** and **response-body.adoc** 이다.

### Request and Response Fields
- 다음 예제를 통해 문서화 하는 방법을 살펴보자.
```json
{
	"contact": {
		"name": "Jane Doe",
		"email": "jane.doe@example.com"
	}
}
```
```groovy
RestAssured.given(this.spec).accept("application/json")
	.filter(document("user", responseFields( // (1)
			fieldWithPath("contact.name").description("The user's name"), // (2)
			fieldWithPath("contact.email").description("The user's email address"))))
	.when().get("/user/5")
	.then().assertThat().statusCode(is(200));
```
- (1)
  - 응답 바디에 있는 필드를 설명하는 스니펫 만드는 설정
  - 요청 바디를 문서화하고 싶으면 requestFields() 활용
  - 둘다 PayloadDocumentation 클래스에 있다.
- (2)
  - 해당 값의 필드가 있는지 검사한다.
- 결과로 만들어지는 스니펫엔 필드를 설명하는 테이블이 추가되며, request-fields.adoc, response-fields.adoc 이다.
- body에 있는 모든 필드를 작성하지 않으면 테스트는 실패한다.
- 문서화한 필드가 body에 없을 때 선택 사항으러 마킹하지 않았다면 테스트는 실패한다.
- 모든 필드를 적고 싶지 않고 아래와 같이 하나로 묶어서 문서화하는 것도 가능하다.
```groovy
RestAssured.given(this.spec).accept("application/json")
	.filter(document("user", responseFields(
			subsectionWithPath("contact").description("The user's contact details")))) // (1)
	.when().get("/user/5")
	.then().assertThat().statusCode(is(200));
``` 
- 모든 필드를 문서화하지 않아도 테스트를 실패하지 않도록 하고 싶다면 relaxedRequestFields, relaxedResponseFields를 사용해라
  - 일부 중요한 시나리오만 문서화하기 유용하다.

<br>

### Fields in JSON Payloads
- json 필드는 점이나 괄호([])로 표기힌다.
  - contact.name
  - **괄호는 [] 와 작은 따옴표로 표기한다.**
    - ['contact']['name']
  - 두가지 표기법을 같이 혼용해서 사용할 수 있다.
    - contact['name']
  - 배열은 []로만 식별한다.
- json 자체가 배열로 시작하는 것도 문서화할 수 있다.
- 아래 배열에서 [].id는 모든 객체의 id 필드를 가리킨다.
  ```json
  [
      {
          "id" : 1
      },
      {
          "id" : 2
      }
  ]
  ```
- 이름이 다른 필드를 한번에 매칭하고 싶으면 와일드카드(*)를 사용할 수 있다.
- 예로 모든 사용자의 role을 문서화하고 싶을 때는 users.*.role
```json
  {
    "users" : {
		"ab12cd34":{
			"role": "Administrator"
		},
		"12ab34cd":{
			"role": "Guest"
		}
	}
  }
```

<br>

### JSON Field Types
- 지원하는 필드 타입들은 다음과 같다.
  - array
  - boolean
  - object
  - number
  - null
  - string
  - varies : 특정 필드를 각기 다른 타입으로 여러번 하는 경우 사용
- FieldDescriptor.type()을 통해 직접 타입을 설정할 수 있다.
- **문서화할 때 object를 넘겨주는 경우 toSting() 메서드를 호출한다.**
```groovy
RestAssured.filter(document("user", responseFields(
        fieldWithPath("contact.email")
                .type(JsonFieldType.STRING) 
				.description("The user's email address"))))
```
