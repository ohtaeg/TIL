# S3 CORS
- Origin 이란?
  - 프로토콜, 호스트, 도메인, 포트를 뜻한다. 
- Cross Origin Resource Sharing, 리소스를 다른 Origin에서 얻을 수 있다는 뜻
- 웹 브라우저에서 기본적인 보안
- 웹(A)에서 다른 오리진(B)으로 방문하려 할 때 B가 허락할 때에만 A가 요청을 보낼 수 있도록 허락하는 설정
- 올바른 CORS 헤더가 없으면 웹 브라우저가 해당 요청을 차단한다.
- B에서 CORS 헤더를 허용하지 않는 한 요청이 수행될 수 없다. (ex, Access-Control-Allow-Origin)
1. 웹 브라우저가 첫번째 웹 서버를 방문한다. 첫번째 방문하는 서버를 오리진(A)이라고 한다.
2. 두번째 웹서버는 다른 오리진(B)이 된다.
3. 오리진에서 업로드 파일에 대한 요청을 B를 통해 해결할 수 있을 때 웹 브라우저는 B에게 사전 요청이라는 것을 전송한다.
   - Preflight Request
   - HTTP METHOD : OPTIONS
   - HOST : B origin
   - Origin : A
   - B에서 웹 브라우저에게 주는 응답은 다음과 같다. 
   - Preflight Response
   - Access-Control-Allow-Origin : "A HOST"
   - Access-Control-Allow-Methods : "GET, PUT, DELETE"
4. 응답을 통해 웹 브라우저는 B에게 요청을 보낸다.

<br>

- 클라이언트가 웹 사이트로 활성화 된 S3 버킷에 대해 CORS를 요청하는 경우 올바른 헤더를 활성화해야한다.
- 전체 오리진 이름을 지정 (*)해서 허락하거나 특정 오리진에 대해 허용할 수 있다.
1. 웹 브라우저가 첫번째 버킷(A)에서 HTML을 가져온다. 이때 버킷은 웹사이트로 활성화 된 상태이다.
2. 두번째 버킷은 교차 오리진 버킷(B)이 되며 웹사이트로 활성화 되어있고 다른 파일을 가지고 있다.
3. 웹 브라우저는 A에게 html을 요청하고나서 B에게 이미지를 요청할 때 B 버킷이 CORS 헤더를 허용하면 요청 허락
- **`CORS 헤더는 첫번째 오리진 버킷이 아닌 교차 오리진 버킷에 구성되어야 한다.`**