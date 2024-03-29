# Topic이란?
- 데이터가 들어가는 공간
- DB의 테이블, 파일시스템의 폴더와 유사한 성질을 가지고 있다.
- 토픽을 여러개 생성할 수 있으며 목적에 따라 토픽 이름을 다르게 지정할 수 있다.
- 이름을 명명함으로써 어떤 데이터를 관리하는지 유지보수의 장점이 있다.

<br>

## 하나의 토픽은 여러개의 파티션으로 구성될 수 있다.
- 첫번째 파티션은 0번부터 시작한다.
- 하나의 파티션은 큐와 같이 데이터가 쌓이게 된다.
- 컨슈머는 파티션의 데이터가 가장 오래된 순서대로 데이터를 소비한다.

<br>

## 컨슈머가 토픽 내부의 파티션에서 데이터를 소비하더라도 삭제되지 않는다.
- 파티션에 그대로 남아있게 된다.
- 그렇다면 누가 남은 데이터를 가져가는가?
- 새로운 컨슈머가 붙게되면 다시 0번부터 가져가서 사용한다.
- 이때 **서로 다른 컨슈머 그룹이여야하며 auto.offset.reset=earliest** 여야 한다.
- 이렇게되면 동일 데이터를 두번 처리하게 되는데 이런 경우는 다음과 같다.
  - 데이터를 특정 컨슈머그룹에서 처리하고 다른 컨슈머그룹은 데이터를 분석하거나 시각화하기 ES에 저장하거나 백업을 위해 하둡에 저장하는 등의 경우

<br>

## 파티션의 데이터는 언제 삭제되는가?
- 데이터는 옵션에 따라 타이밍이 다르다.
  - log.retention.{ms,minutes,hours} : 최대 record 보존 시간, 기본값은 7이며 토픽별로 정의 가능.
  - log.retention.byte : 최대 record 보존 크기 (byte), 기본값은 -1이며 토픽별로 정의 가능.
  - 레코드의 최대 시간과 크기를 지정할 수 있다.

<br>

## 파티션이 2개 이상인 경우
- a라는 데이터는 어느 파티션에 들어가야할까?
- 프로듀서가 데이터를 보낼 때 `키`를 지정할 수 있다.
  - **키가 null이고 기본 파티셔너 설정을 사용할 경우 라운드 로빈 방식으로 할당**
  - **키가 있고 기본 파티셔너 설정을 사용할 경우 키의 해시 값을 구하여 특정 파티션에 할당**
- **파티션 증가는 주의해야한다. 늘어난 파티션은 다시 줄일 수 없다.**