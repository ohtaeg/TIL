# AWS Global Accelerator

- 만약 우리 어플리케이션이 한 리전에 배치되어있는 제한적인 상황에서 글로벌한 사용자들이 접근하는 경우 빠른 응답을 위해 가장 가까운 엣지 로케이션으로 라우팅하는 방법
- Global Accelerator는 애니캐스트 IP를 사용한다.
  - 유니캐스트 IP : 하나의 서버가 하나의 IP 주소를 가진다.
  - 애니캐스트 IP : 모든 서버가 동일한 IP주소를 가지며 클라이언트는 가장 가까운 서버로 라우팅 된다.
- 두개의 고정 애니캐스트 IP가 생성된다. 둘 다 글로벌하다.
- 어플리케이션을 라우팅하기 위해 AWS 사설 글로벌 네트워크를 활용한다.
- EIP, EC2, ALB, NLB와 함께 작동한다.
- 낮은 지연시간과 빠른 리전 장애 조치가 이루어진다.
- 아무것도 캐시하지 않기에 클라이언트 캐시와 문제가 없다.
- Global Accelerator가 어플리케이션에 대해 상태 확인(Health Check)을 실행한다.
  - 그리고 어플리케이션이 글로벌한지 체크한다.
  - 상태확인을 실패하면 1분안으로 장애 조치가 이루어진다.
- 두 개의 외부 IP만 존재하기 때문에 보안 측면에서도 매우 안전하다.
  - 자동으로 DDoS 보호도 받는다. (AWS Shield)



<br>



## Global Accelerator vs CloudFront

- 둘 다 동일한 글로벌 네트워크를 사용
- 전 세계의 엣지 로케이션을 사용한다.
- DDoS 보호를 위해 AWS Shield와 통합된다.

<br>

CloudFront

- 이미지나 비디오처럼 캐시 가능한 내용과 API 가속 및 동적컨텐츠들에게 성능을 향상시킨다.
- 이 내용들은 엣지 로케이션으로부터 제공된다.



Global Accelerator

- TCP나 UDP상의 다양한 어플리케이션 성능을 향상시킨다.
- 패킷은 엣지 로케이션에서 하나 이상의 AWS Region에서 실행 중인 어플리케이션으로 프록시된다.
  - 즉, 모든 요청이 어플리케이션 쪽으로 전달이된다.
- 캐싱은 불가능하다.
- 게임이나 IoT, Voice over IP 같은 HTTP가 아닌 곳에 사용할 경우 적합하다.
  - 글로벌하게 고정 IP를 요구하는 HTTP를 사용할 때 쓰인다.



<br>



## Global Accelerator 생성시

- Client affinity (밀접성)은 글로벌 밸런서에 고정을 원할 때 사용한다.
  - 동일한 사용자가 동일한 엔드 포인트로 돌아가도록 할 때 사용한다.
- Traffic dial은 가중치인데, 설정한 가중치만큼 트래픽이 해당 리전으로 간다는 뜻









