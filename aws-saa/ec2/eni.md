# Elastic Network Interfaces (ENI)
- 탄력적 네트워크 인터페이스
- VPC 내에 인스턴스에 붙는 가상 네트워크 카드

<br>

### ENI 구성 요소
- VPC의 IPv4 주소 범위 중 기본 private IPv4를 가질 수 있다.
    - 모든 인스턴스는 eth0이라는 기본 네트워크 인터페이스를 갖고 시작
    - 인스턴스는 eth0의 IP가 있는 서브넷에 속한다.
- 1개 이상의 예비 private IPv4를 가질 수 있다.
- private IPv4 당 한 개의 EIP를 가질 수 있다.
- 1개 이상의 Public IPv4를 가질 수 있다.
- 시큐리티 그룹이 붙는 대상이다. 즉 시큐리티 그룹은 ENI에 붙는다.
    - VPC 퍼블릭 서브넷에 위치한 EC2 인스턴스에 엑세스할 경우 ENI를 통해 EC2에 엑세스가 가능하다.
    - ENI에 1개 이상의 시큐리티 그룹을 설정할 수 있다.
- 맥주소를 가진다.
- EC2에서 ENI를 독립적으로 생성할 수 있다.
- AZ에 묶여있다.
    - ENI를 특정 AZ에 생성할 수 있따.
    - `특정 AZ에 바인딩`되기에 ENI를` 다른 AZ의 EC2 인스턴스에 연결할 수 없다.`
- ENI를 다른 인스턴스에 재할당할 수 있다. 즉 ENI에 할당된 설정들이 따라간다.
- 인스턴스 장애 발생 시 인터페이스 및/또는 보조 프라이빗 IPv4 주소를 스탠바이 인스턴스로 이동할 수 있다.
- 인스턴스가 작동하지 않는 경우 사용자 또는 실행 중인 코드는 네트워크 인터페이스를 핫 스탠바이 인스턴스에 연결할 수 있다.
  - 네트워크 인터페이스를 대체 인스턴스에 연결하자마자 네트워크 트래픽이 스탠바이 인스턴스로 전달되고, 인스턴스에 장애가 발생한 시간과 네트워크 인터페이스가 대기 인스턴스에 연결되는 시간 사이에 잠시 연결이 끊기지만 라우팅 테이블 또는 DNS 서버에 대해 어떠한 변경도 수행할 필요가 없다.

----

https://dev.classmethod.jp/articles/amazon-vpc-eni-deep-dive/

https://aws.amazon.com/ko/blogs/korea/announcing-improved-vpc-networking-for-aws-lambda-functions/

람다 공부할때 hyperplane 에 대해서 다시 공부해보기