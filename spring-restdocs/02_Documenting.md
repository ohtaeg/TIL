# Documenting your API
- 해당 목차는 [공식 문서](https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/#documenting-your-api)에서 REST Docs로 API를 문서화 하는 방법을 읽고 정리한다.

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

<br>

### Reusing Field Descriptors
- 생략, 필요할 때 정리

<br>
<br>

## Documenting a Subsection of a Request or Response Payload
- payload가 크거나 복잡하면 섹션별로 분리해서 문서화하는 것도 유용하다.
- payload의 하위 섹션을 추출하여 문서화할 수 있다.

### Documenting a Subsection of a Request or Response Body
- 아래 JSON 응답 바디를 살펴보자.
```json
{
	"weather": {
		"wind": {
			"speed": 15.3,
			"direction": 287.0
		},
		"temperature": {
			"high": 21.2,
			"low": 14.8
		}
	}
}
```
- 이때 하위 객체인 temperature만 문서화하는 스니펫을 다음과 같이 만들 수 있다.
```groovy
RestAssured.given(this.spec).accept("application/json")
	.filter(document("location", responseBody(beneathPath("weather.temperature")))) // (1)
	.when().get("/locations/1")
	.then().assertThat().statusCode(is(200));
```
- (1)
  - 응답 바디의 하위 섹션(weather.temperature)만 가지는 스니펫을 만든다.
  - PayloadDocumentation의 responseBody()와 beneathPath()을 이용하면 된다.
- 위 설정으로 만들어지는 스니펫은 아래와 같다.
```json

{
	"temperature": {
		"high": 21.2,
		"low": 14.8
	}
}
```
- 스니펫 이름은 beneath-${path} 형식으로, response-body-beneath-weather.temperature.adoc 으로 만들어진다.
- withSubsectionId("name") 을 활용하면 이름을 커스텀하게 만들 수 있따.

> responseBody(beneathPath("weather.temperature").withSubsectionId("temp"));

- request-body-temp.adoc

<br>

### Documenting the Fields of a Subsection of a Request or Response
- 생략, 필요할 때 정리

<br>
<br>

## Request Parameters
- 요청 파라미터는 requestParameters()로 문서화할 수 있으며 쿼리스트링으로 추가하는 방법은 다음과 같다.
```groovy
RestAssured.given(this.spec)
	.filter(document("users", requestParameters( // (1)
			parameterWithName("page").description("The page to retrieve"), // (2)
			parameterWithName("per_page").description("Entries per page")))) // (3)
	.when().get("/users?page=2&per_page=100") // (4)
	.then().assertThat().statusCode(is(200));
```
- (1)
  - 요청 파라미터를 설명하는 스니펫
- (2), (3)
  - 4번의 쿼리스트링을 설명
- (4)
  - 쿼리스트링에 두파라미터를 사용해서 get 요청 수행
- 요청 파라미터가 실제 요청에 없을 때 선택사항으로 설정하지 않았으면 테스트는 실패한다.
- 모든 요청 파라미터를 문서화하지 않아도 테스트를 실패하지 않도록 relaxedRequestParameters()를 사용할 수 있다.

<br>

### Path Parameters
```groovy
RestAssured.given(this.spec)
	.filter(document("locations", pathParameters( // (1)
			parameterWithName("latitude").description("The location's latitude"), // (2)
			parameterWithName("longitude").description("The location's longitude")))) // (3)
	.when().get("/locations/{latitude}/{longitude}", 51.5072, 0.1275) // (4)
	.then().assertThat().statusCode(is(200));
```
- (1)
  - 요청 패스 파라미터를 설정하는 스니펫
- (2), (3)
  - RequestDocumentation.parameterWithName()을 사용하여 패스 파라미터 문서화
- (4)
  - 패스 파라미터를 통해 GET 요청 수행

<br>

## Request Parts
- 멀티파트 요청도 requestParts로 문서화할 수 있다.
```groovy
RestAssured.given(this.spec)
	.filter(document("users", requestParts( // (1)
			partWithName("file").description("The file to upload")))) // (2)
	.multiPart("file", "example") // (3)
	.when().post("/upload") // (4)
	.then().statusCode(is(200));
```

### Request Part Payloads
- 요청 body의 part는 위 요청 body와 동일한 방식으로 문서화할 수 있다.

### Documenting a Request Part’s Body
- 생략, 필요할 때 정리

### Documenting a Request Part’s Fields
- 생략, 필요할 때 정리

<br>
<br>

## HTTP Headers
- requestHeaders, responseHeaders를 통해 요청, 응답 헤더를 문서화할 수 있다.
```groovy
RestAssured.given(this.spec)
	.filter(document("headers",
			requestHeaders( // (1)
					headerWithName("Authorization").description(
							"Basic auth credentials")), // (2)
			responseHeaders( // (3)
					headerWithName("X-RateLimit-Limit").description(
							"The total number of requests permitted per period"),
					headerWithName("X-RateLimit-Remaining").description(
							"Remaining requests permitted in current period"),
					headerWithName("X-RateLimit-Reset").description(
							"Time at which the rate limit period will reset"))))
	.header("Authorization", "Basic dXNlcjpzZWNyZXQ=") // (4)
	.when().get("/people")
	.then().assertThat().statusCode(is(200));
```

<br>
<br>

## Documenting Constraints
- ConstraintDescriptions를 이용하여 제약조건 문서화도 가능하다.
```groovy
public void example() {
	ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserInput.class); // (1)
	List<String> descriptions = userConstraints.descriptionsForProperty("name"); // (2)
}

static class UserInput {

	@NotNull
	@Size(min = 1)
	String name;

	@NotNull
	@Size(min = 8)
	String password;

}
```
- (1)
  - UserInput 클래스의 제약조건 설명 문서를 생성한다.
- (2)
  - name 필드의 제약조건들을 가져온다. 현재 예로 NotNull과 Size 제약 조건이 담겨있다.

<br>

### Reusing Snippets
- 스니펫을 재사용할 수 있다. 만들어진 스니펫에 discriptor를 추가할 수 있다.
```groovy
RestAssured.given(this.spec)
	.accept("application/json")
	.filter(document("example", this.pagingLinks.and( 
			linkWithRel("alpha").description("Link to the alpha resource"),
			linkWithRel("bravo").description("Link to the bravo resource"))))
	.get("/").then().assertThat().statusCode(is(200));
```

<br>

### Finding Constraints
- 기본적으로 Bean Validation Validator를 찾는다.
- `현재는 프로퍼티 제약 조건만 지원한다.`
- 커스텀 Valiator를 문서화하고 싶다면 ValidatorConstraintResolver를 인스턴스로  ConstraintDescriptions를 만들면 사용할 Validator를 커스텀할 수 있다.
- 제약조건 resolution(?)을 완벽하게 제어하고 싶으면 ConstraintResolver를 직접 구현해라.
- 
