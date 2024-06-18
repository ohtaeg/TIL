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

마스터 슬레이브 구조에서 센티널이라는 redis와 별도의 프로세스가 감시하고 있다가 마스터가 다운되면 자동으로 슬레이브를 마스터로 승격시켜준다.

모니터링, auto failover, 알림 등의 기능을 제공한다.

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


## 센티넬 노드는 왜 홀수로 구성해야하는가?
최소 3개 이상 인스턴스와 갯수는 홀수로 만들기를 권장한다.

짝수case에서 50%투표율일때 재투표로 이어짐

Redis 상태 체크 시 센티넬 인스턴스들의 다수결에 의해 결정되기 때문에 홀수로 구성해야 한다.

과반 수 이상으로 결정하는 이유는 만약 어느 sentinel이 단순히 네트워크 문제로 master와 연결되지 않을 수 있는데, 그 때 실제로 Master는 다운되지 않았으나 연결이 끊긴 sentinel은 Master가 다운되었다고 판단할 수 있기 때문이다

<br>

## failover 동작 방식
Sentinel은 주기적으로 master, slave 및 기타 sentinel들에 대하여 위에 언급한 방법들로 health체크를 한다.




<br>

### S Down, O Down
#### SDOWN
- Subjectively down(주관적 다운)
- Sentinal에서 주기적으로 Master에게 보내는 PING과 INFO 명령의 응답이 3초(down-after-milliseconds 에서 설정한 값) 동안 오지 않으면 주관적 다운으로 인지

#### ODOWN
- Objectively down(객관적 다운)
- 설정한 quorum 이상의 Sentinal에서 해당 Master가 다운되었다고 인지하면 객관적 다운으로 인정하고 장애 조치를 진행

ODOWN상태는 master에만 적용되는 상태이며 failover액션으로 이어진다. replica 다운시 sentinel이 취하는 액션이 없으며 따라 replica는 ODOWN상태가 없다.

## 마스터가 죽었다가 다시 살아나면 마스터로 다시 승격되는가?
Sentinal 인스턴스 과반 수 이상이 Master 장애를 감지하면 Slave 중 하나를 Master로 승격시키고 기존의 Master는 Slave로 강등시킨다.

<br>




<br>

## Replica selection and priority
- https://velog.io/@roycewon/Redis-Sentinel

- 1차 복제 서버 중 replica-priority 값이 가장 작은 서버가 마스터에 선정된다. 0으로 설정하면 master로 승격 불가능하고 동일한 값이 있을 땐 엔진에서 선택한다

<br>


## 센티넬 단점
- 모든 쓰기 작업은 마스터 노드에서 처리되기 때문에 마스터 노드가 성능 병목이 될 수 있다.
- 슬레이브 노드가 마스터 노드로부터 지속적으로 데이터를 복제하기 때문에 네트워크 부하가 증가할 수 있다.

<br>

## 주의해야할 점
#### get-master-addr-by-name
Master, Slave 모두 다운되었을 때 Sentinel에 접속해 Master 서버 정보를 요청하면 다운된 서버 정보를 리턴한다. 

따라서 INFO sentinel 명령으로 마스터의 status를 확인해야 한다.

#### 마스터 노드의 Failover Timeout 시간 만큼 데이터의 쓰기가 실패하게 된다.

#### THEN
쓰기 실패에 대비해서 장애 발생 시 db에서 데이터를 가져와서 리턴할 수 있도록 한다.

<br>

#### Slave다운 → Master다운 → 다운된 Slave재시작되면 이 서버는 Master로 전환되지 않는다.
slave의 conf에 복제서버라고 설정이 되어 있고 센티넬에서도 복제라고 인식하고 있기 때문
#### THEN
slave의 레디스 설정 파일 redis.conf에 있는 slaveof 파라미터 삭제 후 구동해야 한다.

<br>

#### Sentinel은 1차 복제만 Master 후보에 오를 수 있다. ( 복제 서버의 복제 서버는 불가능 )

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