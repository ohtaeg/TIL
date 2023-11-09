# Lambda@Edge로 SPA 환경에서 SEO 최적화 하는 법
- https://uzihoon.com/post/8f203610-8e67-11ea-925d-abc1dfcb23bd

## SPA의 문제점

### 1. 초기에 모든 리소스들을 받아오므로 로딩 시간이 길어진다.
- SPA에서는 최초 모든 리소스들을 불러오기 때문에 특정 페이지에 진입하지 않더라도 관련된 리소스들을 모두 가져온다.
  - 이렇게 될 경우 최초 화면 진입시에 로딩시간이 조금 걸리게 된다. 
- 몇 초 정도의 로딩시간은 무관할 경우 큰 문제는 없지만 사용자 경험이 최우선 되는 서비스라면 고려 대상이다.
- 이러한 문제를 해결하기 위해선 lazy하게 해당 페이지 진입시 리소스들을 불러오게 하거나 렌더링 방식을 변경함으로써 해결할 수 있다.

### 2. 크롤링을 하는 봇이 어떤 문서인지 인식을 할 수가 없다. (구글 크롤링 봇 제외)
- 크롤링 봇은 html 문서 내의 meta 태그나 본문에 들어있는 내용을 읽어서 인식을 하는데, SPA에서는 최초 빈 html 파일에 javascript로 포함될 내용을 넣어주므로 최초에는 빈 html 파일 밖에 없다.
- 이때 크롤링 봇이 어떤 화면인지 분석할 수 없으므로 SEO가 어렵다.

<br>

## SPA환경에서 SEO 최적화 하는 방법
### AWS CloudFront와 Lambda@Edge
- AWS CloudFront는 CDN(Content Delivery Network) 서비스로 S3 URL을 연결해 줌으로써 단순히 파일을 제공하도록 설정할 수 있다. 
- 이때 특정한 시점에 Lambda 함수를 실행 시켜주도록 하는데 이때 실행되는 것을 Lambda@Edge 라고 부른다.
- Lambda@Edge는 네가지 상황에서 실행 된다.
1. `viewer-request`
    - 클라이언트로부터 CloudFront에 요청이 도착했을 때
2. `origin-request`
    - Origin Server에 요청하기 전
3. `origin-response`
    - Origin Server로 부터 응답이 도착했을 때
4. `viewer-response`
    - 클라이언트에게 응답을 돌려 주기 전

- 클라이언트는 서비스를 이용하는 고객 or 크롤링 봇일 수 있다. 
- 그렇다면 현재 요청이 봇인지 아닌지를 판별하여 응답을 하기 전에 크롤링에 필요한 데이터를 본문에 채워서 제공을 한다면 되지 않을까?
  - 그렇게 하기 위해서는 두가지 상황에서 작업이 필요하다.

1. viewer-request : 요청이 온 클라이언트가 봇인지 아닌지 구분
2. origin-Response : 봇이라면 응답을 해주기 전 데이터를 채워서 보내주어야 한다.

- Lambda@Edge를 사용하기 위해선 몇가지 제약 사항과 유의사항이 있다.
  - viewer-request는 128MB Memory와 5초 timeout 제한이 있으며
  - origin-response는 3008MB Memory와 30초 시간 제한이 존재
  - Runtime 환경 언어 제한

- 람다 코드 작성시 viewer-request 작업은 request 헤더에 있는 정보로 user-agent가 봇인지 아닌지를 구분하고
- origin-response에서는 두가지를 체크한다.
  - post/* 로 들어온 요청인지 
  - 크롤러 봇인지
  - 두가지 모두 해당될 경우 S3와 DB에서 관련된 정보를 가져와 HTML 파싱 후 해당 내용을 body에 담아서 callback으로 리턴해주기만 하면 된다.