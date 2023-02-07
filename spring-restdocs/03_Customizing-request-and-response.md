# Customizing requests and responses
- 테스트 코드에 작성한 요청, 응답과 일치하지 않는 문서를 작성하고 싶을때 `전처리기`를 이용하여 커스텀마이징이 가능하다.
  - OperationRequestPreprocessor.preprocessRequest()
  - OperationResponsePreprocessor.preprocessResponse()
  - document() 호출시 사용 가능

```groovy
RestAssured.given(this.spec).filter(document("index", 
        preprocessRequest(modifyHeaders().remove("Foo")), // (1)
		preprocessResponse(prettyPrint())) // (2)
).when().get("/").then().assertThat().statusCode(is(200));
```
- (1)
  - 요청 헤더를 제거하는 전처리기 적용
- (2)
  - 응답 컨텐츠를 보기 좋게 출력해주는 전처리기 적용, 요청도 가능
- 모든 테스트에 같은 전처리기를 적용하고 싶으면 @Before에 위 설정과 같이 전처리기를 적용하면 된다.

<br>

## Preprocessors

### Modifying Headers
- modifyHeaders()는 요청, 응답에서 헤더를 추가, set, 제거를 할 수 있다. 

### Modifying URIS
- modifyUris()는 요청, 응답에서 모든 URI를 수정할 수 있다.
- 로컬 인스턴스 테스트시 문서에 보이는 URI만 따로 커스텀할 수도 있다.