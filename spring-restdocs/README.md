# Spring REST Docs
- 업무를 진행하면서 단순 문서화가 아닌 OAS 추출 등 추가 작업을 진행하다보니 사용법만 익히고 제대로 본적이 없다.
- 이번 기회에 [문서](https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/#introduction)를 읽으면서 정리해나가자. 필요한 부분만 발췌해서 정리한다.

## 1. Introduce
```text
Spring REST Docs uses Asciidoctor by default.
Asciidoctor processes plain text and produces HTML, styled and laid out to suit your needs.
If you prefer, you can also configure Spring REST Docs to use Markdown.
```
- 스프링 REST Docs는 기본적으로 `Asciidoctor`를 사용한다.
- `Asciidoctor`는 텍스트를 처리해서 필요에 맞는 HTML 형식으로 만들고 원하면 Markdown을 사용하도록 설정할 수 있다.

### Asciidoctor란?
```
Asciidoctor is a fast, open source, Ruby-based text processor for parsing AsciiDoc® into a document model 
and converting it to output formats such as HTML 5, DocBook 5, manual pages, PDF, EPUB 3, and other formats.
```
- [Acidoctor](https://asciidoctor.org/)는 ASCIIDoc를 문서 모델로하는 Ruby 기반의 텍스트 프로세서이다.
- HTML 5, DocBook 5, PDF, 등 출력 형식으로 변환이 가능하다.
- 즉 ASCIIDoc 기반의 문서를 HTML 등의 문서로 변환이 가능하게 해주는 변환기라고 해석이 된다.

<br>

## 2.2 Requirements
- 최소 환경은 다음과 같다.
  - JAVA 8
  - SPRING 5.0.2 OR later

## 2.3 Build configuration
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

