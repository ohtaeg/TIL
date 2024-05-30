# Redis 그 외 기능들


## TTL
- 데이터를 특정시간 이후 만료시키는 기능
- 초단위
- 데이터 만료되자마자 삭제하지 않고 만료로 표시해놓은 후 백그라운드에서 주기적으로 삭제된다.
  - 데이터 조회시 조회되지 않음

```redis
$ SET gretting hello

-- 남은 만료시간 확인, -1이 리턴된다면 만료가 설정되지 않음을 뜻함
$ TTL greeting
(integer) -1

-- 10초후 만료
$ EXPIRE greeting 10
(integer) 1

-- 남은 만료시간 확인, -2가 리턴된다면 만료되었다는 의미
$ TTL greeting
(integer) -2

-- 데이터 set과 동시에 만료시간 설정
$ SETEX greeting 10 hello
```

<br>

## SET NX/XX 옵션
- SET 명령어 **옵션**
- NX
  - 해당 key가 존재하지 않는 경우에만 SET
- XX
  - 해당 key가 존재하는 경우에만 SET

```redis
$ SET gretting hello NX
OK

-- 이미 키가 존재하기 때문에 실패
$ SET gretting hello NX
nil

$ SET gretting hi XX
OK

-- key가 존재하지 않기 때문에 실패
$ SET invalid hello XX
nil
```

## Pub / Sub
- 시스템간 메시지 통신 패턴 중 하나로 publisher 와 subscriber가 서로 알지 못해도 통신이 가능한 디커플링 패턴
- Stream 과 다른점은 메시지가 보관되는 Stream과 달리 Pub/Sub은 fire and forget 메커니즘 (실행 후 무시)이 적용되어 있어 구독하지 않을 때 발행된 메시지 수신 불가

```redis
-- 구독 레디스쪽에서 order와 결제 채널 구독
sub> $ SUBSCRIBE ch:order ch:payment

-- 발행 레디스 쪽에서 order 채널에 new-order라는 메시지 발행
pub> $ PUBLISH ch:order new-order

-- 발행 레디스 쪽에서 payment 채널에 card라는 메시지 발행
pub> $ PUBLISH ch:payment card
```

<br>

## Pipeline
- 다수의 레디스 명령어를 한번에 요청하여 네트워크 성능을 향상시키는 기능
  - 한번에 전달하는 기술로써 다수의 명령어가 개별적으로 수행
- 일반적으로 req - res 모델은 네트워크를 통해 트래픽이 오고가며 라운트 트립 타임이 발생
- 파이프라이닝은 라운드 트립의 횟수를 줄여 네트워크 시간을 최소화