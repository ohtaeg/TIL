# S3 보안 설정
1. JSON based Bucket Policy
   - 버킷 단위, 버킷 내에 파일 하나하나 설정은 불가능하고 모든 설정은 버킷단위로 적용
   - S3 버킷에서 보안 주차게 무엇을 할 수 있고 무엇을 할 수 없는지 결정하는 정책
   - 교차 계정 엑세스가 활성화된다.
   - 버킷 정책을 사용하는 경우는 
     1. 보통 버킷에 퍼블릭 엑세스 권한을 승인하는 경우
     2. 업로드 시점에 객체를 암호화시킬 경우 
     3. 교차 계정 S3 버킷 정책을 사용하여 다른 계정에 엑세스 권한을 주는 경우
   - JSON 형식, IAM Policy와 비슷하다.
   - ```json
     {
       "Version" : "",
       "id" : "",
       "statement" :  [
         {
           "sid" : "",
           "effect" : "Allow", // Allow or Deny
           "principal" :  {"AWS" :  "arn:aws:iam::spark323:user/spark"}, // 버킷 정책 적용 대상
           "action" : ["s3:GetObject"], // API 접근 허가 및 거부
           "resource" : ["arn:aws:s3:::my-bucket/*"], // 버켓 혹은 객체들
           "condition" : {}
         }    
       ]
     }
     ```
2. ACL
   - 파일 단위 (객체)로 엑세스 규칙을 설정
3. S3 Access Log
   - S3에 보내지는 모든 요청은 계정 승인 여부에 상관 없이 다른 버킷 혹은 다른 계정으로 로그 전송(저장) 가능
   - 자기 계정이 털리면 해킹자가 버킷 접근이 가능하기 때문에 다른 버킷에 로그를 관리하여 로그 삭제해버리거나 다른 버킷에서 관리하는 방법
   - CloudTraild, Athena 등을 통해 로깅할 수 있다.
   - 모니터링 중인 버킷을 로깅 버킷으로 설정하면 안된다. (어플리케이션 버킷과 로깅 버킷을 분리해야한다.)
     - 로깅 루프가 생기고 버킷의 크기가 기하급수적으로 커진다.
4. MFA 활용하여 삭제 방지 가능
   - 특정 버전 객체를 버킷에서 삭제하고 싶은 경우 
5. User based IAM Policy
   - IAM 콘솔로부터 IAM 사용자는 IAM 정책을 가지고 있는데 정책을 통해 어떤 API 호출을 허용될지 결정할 수 있다.
   - 유저나 역할 등은 IAM 권한이 허용할 경우 S3 객체에 엑세스할 수 있다.
   - **대신 연결된 IAM 정책의 명시적인 거부는 없어야한다.** 
     - 사용자가 IAM 정책을 통해 S3 버킷에 엑세스할 수 있더라도 버킷 정책이 사용자 엑세스를 명시적으로 거부한다면 엑세스가 불가능하다.
     - IAM 정책의 명시적 DENY는 S3 버킷 정책보다 우선시 하기 때문에 만약 IAM 사용자가 S3 버킷의 파일을 읽고 쓸 수 있도록 S3 버킷 정책을 업데이트 했지만 사용자 중 한 명이 API 호출을 못할 수 있다.
6. Pre-Signed URLS (사전 서명된 URL)
   - AWS 자격 증명으로 서명된 URL
   - S3 버킷의 일부 작업에 대한 시간 제한 액세스 권한을 부여하기 위해 생성하는 임시 URL
     - 제한된 시간내에 특정 사용자가 특정 파일에 엑세스하는 경우
     - 한정된 시간 동안만 유효

<br>

## S3 객체 암호화
- S3 데이터의 암호화 방법은 다음과 같다.
- 전송 중에는 SSL / TLS (HTTPS)
  - S3가 HTTPS를 주도한다.
  - 암호화 되지 않은 상태의 HTTP endpoint 도 제공한다.
  - 기본적으로 암호화된 HTTPS 엔드 포인트를 노출하여 전송중 암호화를 제공한다.
- 저장이 되었을 때 객체는 버킷 정책을 사용하는 4가지 암호화 방법이 있다.
- 버킷 정책을 사용하면 API 호출에서 암호화 헤더가 지정되지 않는다면 요청이 거부된다.
1. SSE-S3
   - S3에서 알아서 암호화
   - AES-256 알고리즘
   - 헤더에 `"x-amz-server-side-encryption" : "AES256"` 로 설정 해야한다.
2. SSE-KMS
   - KMS 서비스를 이용해 암호화
   - SSE-S3 대신 사용하는 이유는 `누가 어떤 키에 접근할 수 있는지에 대해 제어 하기 위함` + `누가 접근했는지 추적 가능`
   - 헤더에 `"x-amz-server-side-encryption" : "aws:kms"` 로 설정 해야한다.
3. SSE-C
   - 사용자가 제공한 암호를 통해 암호화, 즉 암호화 키를 AWS 외부에서 사용자가 관리한다.
   - S3는 고객이 제공한 암호화 키를 저장하지 않는다. 암호화를 위해 사용만 하고 바로 폐기한다.
   - 헤더를 통해 암호화 키를 전달한다. 암호화 키를 전달해야하기 때문에 무조건 HTTPS 통신을 해야한다.
4. 클라이언트가 직접 암호화
   - S3에 업로드 하기전에 직접 암호화를 하여 업로드 하는 방법
     - Amazon S3 Encryption Client library도 제공한다.
   - 다운로드 받은 암호화된 데이터 해석도 클라이언트가 직접한다.
- 위 방법 외에 기본 암호화 옵션도 존재한다.
- 암호화되지 않는 객체를 S3에 업로드하면 기본 암호화 옵션을 통해 암호화를 할 수 있다.
- 암호화된 객체를 업로드할 때 재암호화가 되진 않는다.
- 버킷 정책 방식이 기본 암호화 보다 먼저 고려가 된다.
  - ex) SSE-S3 라는 암호화 메커니즘을 강제하려면 버킷 정책을 사용해야한다.

<br>

## 블록 퍼블릭 엑세스 버킷 설정
- 객체가 퍼블릭화 되는것을 차단하는 설정
- 계정에 제한이 있을경우 사용
- 네가지 종류의 퍼블릭 엑세스 차단 설정이 있다.
- 객체와 버킷이 외부로 공개되지 않도록 차단할 수 있게 된다.
1. 새 엑세스 제어목록
2. 모든 엑세스 제어 목록
3. 새 퍼블릭 버킷 or 엑세스 포인트 정책
4. 퍼블릭 버킷 and 엑세스 포인트 정책을 통해 버킷과 객체를 향한 퍼블릭 및 교차 계정 엑세스를 막는 설정
- 기업 데이터 유출을 막기위해 생겼다.

<br>

## 기타 보안
- VPC 엔드 포인트로 S3에 비공개 엑세스 가능