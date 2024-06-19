# Sentinel 
## #. Getting Started
```docker
$ docker-compose up --scale redis-sentinel=3 -d 
```

### Redis master-slave
- 총 3개의 레디스 컨테이너, 1 master - 2 slave
```docker
# redis master
$ docker exec -it redis bin/bash
$ redis-cli
127.0.0.1:6379> role
1) "master"
```


```docker
# redis slave-1 or redis slave-2 
$ docker exec -it redis-slave-1 bin/bash
$ redis-cli
127.0.0.1:6379> role
1) "slave"
```

### Redis sentinel
- 총 3개의 레디스 센티넬 컨테이너
```docker
# redis sentinel
$ docker exec -it spring-redis-sentinel-redis-sentinel-1 /bin/bash
$ redis-cli -p 26379
127.0.0.1:26379> info sentinel
sentinel_masters:1
master0:name=master,status=ok,address=172.22.0.2:6379,slaves=2,sentinels=3
```

## #. 센티넬
레디스는 일반적으로 마스터와 복제로 구성되는데 운영 중 장애 발생시 자동 failover 처리를 위한 고가용성 레디스 모드 중 하나이다.

마스터 슬레이브 구조에서 redis와 센티널이라는 별도의 프로세스가 감시하고 있다가 마스터가 다운되면 자동으로 슬레이브를 마스터로 승격시켜준다.

모니터링, auto failover, 알림 등의 기능을 제공한다.

레디스 공식 문서에서는 모든 프로세스가 서로 다른 장비에 구성되는 것을 권장한다.

Sentinel은 Redis를 observe 하는 것 외에 다른 역할을 수행하지 않아 많은 리소스를 필요로 하지 않기에, 

실제 구성시에는 Redis server 와 함께 구성하기도 합니다.

<br>

## 센티넬과 클러스터 차이
애플리케이션의 요구사항에 맞는 적절한 Redis 모드를 선택하는 것이 중요하다.

|            | Sentinel                 | Cluster                | 
|------------|--------------------------|------------------------|
| 규모         | 중/소 규모의 데이터, 수 기가바이트 이하  | 대규모 데이터, 수십 기가바이트 이상   |
| 샤딩         | 지원 X                     | 지원 O                   |
| 관리         | 비교적 간단한 구성, 데이터가 한곳에서 관리 | 더 많은 구성과 관리, 데이터 분산 저장 |
| 인스턴스 최소 갯수 | 최소 3개                    | 최소 6개                  |
| 인스턴스 최대 갯수 | 최대 10개                   | 최대 1000개               |

<br>

## Sentinel간에는 어떻게 통신하나?
1. **Sentinel 인스턴스들은 Redis의 `Pub/Sub` 메커니즘을 사용하여 서로 통신**

각 센티넬은 `__sentinel__:hello` 채널을 구독한 상태에서 해당 채널에 모니터링하고 있는 마스터, 레플리카 정보에 대한 메시지를 각각 발행하고

각 센티넬은 이 채널의 메시지를 수신하여, 다른 Sentinel들의 존재와 상태를 인식하는 메커니즘이다.

2. **P2P 메시징을 통해 각 Sentinel 인스턴스 간의 상태 정보를 교환**

Sentinel 인스턴스는 주기적으로 다른 Sentinel 인스턴스에게 PING 메시지를 보내 PONG 응답 여부를 확인

그 외에 SENTINEL, and INFO 메시지를 사용하여 서로의 상태를 확인하고, 마스터 노드의 상태 변화를 탐지

<br>

## Sentinel은 Master, Slave와 어떻게 통신하나?
Sentinel은 마스터와 슬레이브 노드 간의 통신을 직접적으로 관리하지 않고 

Sentinel은 주기적으로 마스터 노드와 슬레이브 노드의 정보를 Redis의 `INFO` 명령어를 통해 수집.

해당 명령어를 통해 마스터 or 슬레이브 노드가 다운되거나 문제가 발생한 것을 인식하면

Sentinel은 관련 정보를 Pub/Sub 채널을 통해 다른 Sentinel 인스턴스에 알린다.

<br>
<br>

## failover 동작 방식
- Sentinel은 주기적으로 master, slave 및 기타 sentinel들에 대하여 위에 언급한 방법들로 health체크를 한다.
    - 센티널은 1초에 한 번씩 PING을 보내서 레디스 마스터, 슬레이브, 그리고 다른 센티널들을 체크하는데, 30초 동안 응답이 없으면 해당 서버가 다운된 것으로 인지한다.
    - 여기서 30초는 디폴트 값으로 이것은 설정 파일(sentinel.conf)에 down-after-milliseconds 파라미터로 변경할 수 있다.

<br>

1. 예로 1개의 마스터와 3개의 센티넬 인스턴스가 있을 때, 센티넬-1 이 마스터 장애를 인식한 경우 다른 센티넬들에게 알린다.
2. 만약 다른 센티넬-2, 센티넬-3이 마스터에게 응답을 받아 정상으로 판단한다면 장애처리는 진행하지 않는다. (S-DOWN)
3. 반면 다른 센티넬-2, 센티넬-3이 마스터에게 응답을 받지 못해 `quorum` 값에 맞게 장애라 판단한다면 장애처리를 진행한다. (O-DOWN)
4. 여러 센티넬 인스턴스 중 하나를 failover 작업을 수행할 **sentinel leader를 선출한다.**
5. sentinel leader는 살아있는 slave중 master를 선출하게된다. 기존의 Master는 Slave로 강등시킨다.

요약

1. 마스터 장애 탐지 (quorum 값 사용)
   - S-DOWN, O-DOWN 판단
2. 리더 선출 요청 전송
3. 응답 수집 및 센티넬 리더 결정 (epoch와 sentinel priority 사용, epoch 우선)
4. 센티넬 리더가 failover 수행 (새로운 마스터로 슬레이브 승격)

<br>

### S Down, O Down
#### S-DOWN
- Subjectively down (주관적 다운)
- Sentinal에서 주기적으로 Master에게 보내는 PING과 INFO 명령의 응답이 3초(down-after-milliseconds 에서 설정한 값) 동안 오지 않으면 주관적 다운으로 인지
- **센티널 한 대에서만 판단한 것**으로. 주관적 다운만으로는 **장애조치를 진행하지 않는다.**

#### O-DOWN
- Objectively down (객관적 다운)
- 설정한 quorum 이상의 Sentinal에서 해당 Master가 다운되었다고 인지하면 객관적 다운으로 인정하고 장애 조치를 진행
- O-DOWN 상태는 master에만 적용되는 상태이며 failover 액션으로 이어진다. 
    - replica 다운시 sentinel이 취하는 액션이 없으며 따라 replica는 O-DOWN 상태가 없다.

<br>

## quorum
- Sentinel은 다수결(quorum) 메커니즘을 사용하여 마스터 노드의 장애 여부를 결정
- 마스터 노드의 장애를 판별하는 데 중요한 역할을 하는 값
- 얼마나 많은 Sentinel 인스턴스들이 장애 판단에 동의해야 하는지를 나타내는 값
- 보통 3개의 Sentinel 인스턴스를 구성하고 quorum 값을 2로 설정하는 것이 일반적 
    - 이렇게 하면, 한 Sentinel이 오작동하더라도 나머지 두 Sentinel이 정상적으로 동작할 가능성이 높아지며, 신뢰성 있는 장애 조치를 보장할 수 있다.
- 낮게 가져가면 오탐지와 불필요한 장애 조치가 발생할 수 있다.
- 높게 가져가면 장애 조치가 지연될 수 있으며, 일부 경우 서비스 중단이 지속되거나 장애 조치가 이루어지지 않을 가능성이 있다.

<br>
<br>

## 센티넬 노드는 왜 홀수로 구성해야하는가?
장애 조치(failover) 과정에서 다수결을 통해 신뢰성 있게 결정을 내리기 위함.

최소 3개 이상 인스턴스와 갯수를 권장하며 절반 이상의 동의를 얻기 위해 홀수개로 만들기를 권장한다.

짝수로 만든 경우 50% 투표율일때 재투표로 이어지고 장애 대응이 늦어지거나 합의에 도달하지 못할수 있다.

센티넬 노드를 1개로 구성하지 않는 이유는 sentinel이 단순히 네트워크 문제로 master와 연결되지 않을 수 있는데, 

실제로 Master는 다운되지 않았으나 연결이 끊긴 sentinel은 Master가 다운되었다고 판단할 수 있기 때문이다.

<br>
<br>

## 센티넬 리더 선출 과정
- 센티넬 리더 선출시 중요한 값은 `epoch`, `Priority` 이다.
  - 에포크라는 개념을 이용해 각 마스터에서 발생한 fail over의 버전을 관리함
  - Sentinel이 특정 이벤트(예: failover)를 감지하고 처리할 때마다 config epoch 값이 증가
  
<br>

- **epoch 값이 높다는 것**은 해당 Sentinel 인스턴스가 **가장 최근의 구성 변경** 또는 **장애 조치를 인지하고 처리**했다는 것을 의미
- **즉, 해당 Sentinel이 가장 최신의 상태를 반영하고 있음을 나타낸다.**
    - 최신 상태를 반영하는 Sentinel이 리더가 되면, 장애 조치 과정에서 발생할 수 있는 데이터 불일치나 오류를 최소화

<br>

### 리더 선출 과정
1. 처음으로 마스터 노드를 O-DOWN 으로 인지한 센티널 노드가 우선 에포크(epoch) 값을 하나 증가 시킴 
2. 에포크를 증가시킨 센티널은 다른 센티널 노드들에게 센티널 리더를 선출하기 위해 투표하라는 메시지를 보냄
    - `SENTINEL is-master-down-by-addr <master-ip> <master-port> <current-epoch> <*>`
    - 이 요청에는 `Sentinel의 epoch 값과 우선 순위` 정보가 포함
3. 구독 메커니즘으로 메시지를 받은 다른 센티널 노드가 현재 자신의 에포크보다 전달 받은 에포크가 클 경우 자신의 에포크 증가시킨뒤, 다른 센티널을 리더로 투표하겠다는 응답을 보냄
    - 만약 epoch 값이 동일하다면 전달받은 priority 값으로 비교
4. 구독 메커니즘으로 투표 응답 메시지를 받은 센티널들중 아래 조건을 만족하는 센티넬로 선출된다.
    1) 투표에 참여한 센티널 수가 그 마스터를 모니터링하고 있는 전체 센티널의 과반수 이상이어야 한다.
    2) `자신의 표를 포함해서 자신을 리더로 투표한 수가 쿼럼값 이상이어야 한다.`, `즉 최소 50% + 1의 득표를 얻어야 leader가 될수 있다.`

<br>

블로그에서 종종 리더 선출 과정에서 quorum값이 인용되곤 하는데 정확히는 quorum 값 하나로 리더 선출이 되지 않는다. 리더 선출하는 과정에서 필요한 값 중 하나임.

```
char *sentinelGetLeader(sentinelRedisInstance *master, uint64_t epoch) {
    ...
    
    unsigned int voters = 0, voters_quorum;
    
    ...
    
    voters_quorum = voters/2+1;
    if (winner && (max_votes < voters_quorum || max_votes < master->quorum))
        winner = NULL;

}
```

<br>

## 어느 Slave가 마스터로 승격되는가?
Sentinel 리더는 슬레이브 노드 중 가장 낮은 slave-priority 값을 가진 노드를 우선적으로 고려하며, 

그 다음으로 데이터 동기화 상태, 응답 시간, 접근 가능성을 종합적으로 판단

1. 우선순위(priority)
    - 슬레이브 노드마다 slave-priority라는 값이 있고 우선적으로 slave-priority 값이 낮은 슬레이브 노드를 선택
    - slave-priority 값이 같다면, 2번으로 넘어간다.
2. 데이터 동기화 상태
    - 슬레이브 노드 중 가장 최신 데이터 상태를 가진 노드를 선택
    - 최신 데이터를 가지고 있는 슬레이브 노드는 replication offset이 가장 큰 슬레이브 노드이다.
3. 응답 시간(latency)
    - 가장 빠르게 응답하는 슬레이브 노드 선택

<br>
<br>

## 센티넬 단점
- 모든 쓰기 작업은 마스터 노드에서 처리되기 때문에 마스터 노드가 성능 병목이 될 수 있다.
- 슬레이브 노드가 마스터 노드로부터 지속적으로 데이터를 복제하기 때문에 네트워크 부하가 증가할 수 있다. (이건 클러스터도 동일할 것으로 예상)
- Sentinel + Redis 구조에서 Redis는 비동기 방식으로 데이터 sync를 하기때문에 failover 발생시 부분 데이터 유실 가능성이 존재한다.

<br>

## 주의해야할 점
### get-master-addr-by-name

Master, Slave 모두 다운되었을 때 Sentinel에 접속해 Master 서버 정보를 요청하면 다운된 서버 정보를 리턴한다. 

따라서 INFO sentinel 명령으로 마스터의 status를 확인해야 한다.

<br>

### 마스터 노드의 Failover Timeout 시간 만큼 데이터의 쓰기가 실패하게 된다.
쓰기 실패에 대비해서 장애 발생 시 db에서 데이터를 가져와서 리턴할 수 있도록 한다.

<br>

### Slave다운 → Master다운 → 다운된 Slave재시작되면 이 서버는 Master로 전환되지 않는다.
slave의 conf에 복제서버라고 설정이 되어 있고 센티넬에서도 복제라고 인식하고 있기 때문

slave의 레디스 설정 파일 redis.conf에 있는 slaveof 파라미터 삭제 후 구동해야 한다.

<br>

### Sentinel은 1차 복제만 Master 후보에 오를 수 있다. (복제 서버의 복제 서버는 불가능)

<br>
<br>

## 슬랙으로 알림
Sentinel 자체에는 직접적인 슬랙 알림 기능이 없지만, Sentinel 이벤트 발생시 외부 프로그램을 실행있다.

쉘 스크립트를 통해 슬랙에 발송이 가능하다.

1. 슬랙 웹훅 설정
2. Sentinel 이벤트 발생 시 실행될 쉘 스크립트를 작성
```shell
#!/bin/bash

SLACK_WEBHOOK_URL="https://hooks.slack.com/services/YOUR/WEBHOOK/URL"
SENTINEL_EVENT=$1
SENTINEL_TIMESTAMP=$(date +'%Y-%m-%d %H:%M:%S')

case $SENTINEL_EVENT in
    "+odown" | "+sdown")
        STATUS=":red_circle: Redis Sentinel Alert"
        ;;
    "-odown" | "-sdown")
        STATUS=":green_circle: Redis Sentinel Recovery"
        ;;
    *)
        STATUS=":warning: Redis Sentinel Event"
        ;;
esac

PAYLOAD="{
    \"text\": \"$STATUS\",
    \"attachments\": [
        {
            \"fields\": [
                { \"title\": \"Event\", \"value\": \"$SENTINEL_EVENT\", \"short\": true },
                { \"title\": \"Timestamp\", \"value\": \"$SENTINEL_TIMESTAMP\", \"short\": true }
            ]
        }
    ]
}"

curl -X POST -H 'Content-type: application/json' --data "$PAYLOAD" $SLACK_WEBHOOK_URL
```
3. 스크립트 파일에 권한 부여 및 Sentinel 설정 파일(sentinel.conf)에 스크립트를 등록

<br>

----

## REF
- [레디스 공식문서](https://redis.io/docs/latest/operate/oss_and_stack/management/sentinel/)
- [센티넬 내부 코드](https://github.com/redis/redis/blob/2ad2548747cc83b6aa2097dac3b92ce5c739effe/src/sentinel.c#L110)