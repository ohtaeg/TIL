# S3 공유
- S3 공유는 3가지 방법으로 가능
1. Bucket Policy / IAM
   - 프로그램 엑세스만 가능 (콘솔에서 불가능)
   - `버킷 단위`에서 적용
2. ACL
   - 프로그램 엑세스만 가능 (콘솔에서 불가능)
   - `파일 단위`
3. IAM Cross Account
   - 콘솔 / 프로그램 엑세스 가능
   - 하나의 계정에서 다른 계정에게 권한을 할당

<br>

## S3 Cross Region Replication
- 다른 리전간 복제를 위해서는 버저닝이 활성화 되어 있어야함 (원본, 대상 모두)
  - 다른 Region으로 버킷 복제 가능 (CRR)
    - 규정 준수, 다른 리전으로의 데이터 엑세스 지연 시간 단축, 계정 간 복제
  - 동일한 Region으로 복제 가능 (SRR)
    - 로그 집계에 사용
    - 테스트 <-> 프로덕션 환경 복제 등
- 복제는 비동기적으로 일어난다.
- 복제를 위해선 IAM 역할을 생성해야한다.
- 복제 기능 활성화 전의 데이터는 복제되지 않음
- 버전 삭제 혹은 파일 삭제는 소스에서 대상까지 삭제 마커를 통해 복제할지의 여부를 선택할 수 있는 옵션이 있다.
  - 비활성화하면 삭제시 복제되지 않는다. 활성화하면 삭제된 객체를 다른 버킷에서도 삭제가 된다.
- 특정 버전 ID를 삭제하도록 지정해도 해당 버전은 복제되지 않는다.
  - 복제 규칙은 삭제 사실까지 복제하지 않는게 기본 설정이다.
  - 두 버킷 사이에서 삭제 여부를 복제할 수 있는 방법이 없다.
- 연쇄 복제는 존재하지 않는다.
  - 1 -> 2로 복제하고 2-> 3으로 복제했을 때 1에서 새로 생긴 객체는 2로 복제가 되지만 3으로 복제는 되지 않는다.
