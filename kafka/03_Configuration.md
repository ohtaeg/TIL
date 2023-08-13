# Configuration

- https://kafka.apache.org/documentation.html#configuration
- `Version 3.5.0.RELEASE`

```
Kafka uses key-value pairs in the property file format for configuration. These values can be supplied either from a file or programmatically.
```
- 카프카는 키-밸류 쌍의 프로퍼티 파일 포맷을 사용한다. 파일 또는 프로그래밍적으로 제공할 수 있다.


## Broker Condfigs
```text
The essential configurations are the following:
broker.id
log.dirs
zookeeper.connect
```
- 중요한 설정은 다음과 같다.
- broker.id 
- log.dirs 
- zookeeper.connect

<br>

### advertised.listeners
```
Listeners to publish to ZooKeeper for clients to use, if different than the listeners config property. 
In IaaS environments, this may need to be different from the interface to which the broker binds. 
If this is not set, the value for listeners will be used. 
Unlike listeners, it is not valid to advertise the 0.0.0.0 meta-address.
Also unlike listeners, there can be duplicated ports in this property, so that one listener can be configured to advertise another listener's address.
This can be useful in some cases where external load balancers are used.

Type:	     string
Default:	 null
Importance:	 high
Update Mode: per-broker
```
- 만약 listeners 설정 속성과 다른 경우 클라이언트가 사용하기 위해 주키퍼에 publish할 리스너
- IaaS 환경이라면 브로커가 바인드하는 인터페이스와 달라야할 수도 있다.
- 만약 이것을 설정하지 않으면 (이때 이것은 advertised.listeners를 말하는 것 같다.) listeners 값을 사용한다.
- listeners 다르게, 메타 주소인 0.0.0.0은 유효하지 않다.
- 또한 listeners와 다르게, 이 속성은 중복된 포트를 사용할 수 있으므로, 다른 리스너의 주소를 노출하도록 구성할 수 있다.
- 이것은 외부 로드 밸런서를 사용하면 유용하다.

---
### auto.create.topics.enable
```
Enable auto creation of topic on the server

Type:	     boolean
Default:	 true
Importance:	 high
Update Mode: read-only
```
- 서버에서 토픽 자동 생성을 활성화한다.

---
### auto.leader.rebalance.enable
```
Enables auto leader balancing. 
A background thread checks the distribution of partition leaders at regular intervals, configurable by `leader.imbalance.check.interval.seconds`. 
If the leader imbalance exceeds `leader.imbalance.per.broker.percentage`, leader rebalance to the preferred leader for partitions is triggered.

Type:	     boolean
Default:	 true
Importance:	 high
Update Mode: read-only
```
- 자동 리더 리밸런싱을 활성화한다.
- 백그라운드 스레드는 주기적으로 `leader.imbalance.check.interval.seconds` 설정에 따라 리더 파티션의 배포를 체크한다.
- 만약 리더 불균형이 `leader.imbalance.per.broker.percentage`를 초과한다면, 리더는 선호하는 리더 파티션으로 트리거되어 리밸런싱이 일어난다.

---
### background.threads
```
The number of threads to use for various background processing tasks

Type:	      int
Default:	  10
Valid Values: [1,...]
Importance:	  high
Update Mode:  cluster-wide
```
- 백그라운드 테스크를 처리할 스레드 수

---
### broker.id
```
The broker id for this server. 
If unset, a unique broker id will be generated.
To avoid conflicts between zookeeper generated broker id's and user configured broker id's, generated broker ids start from reserved.broker.max.id + 1.

Type:	     int
Default:	 -1
Importance:	 high
Update Mode: read-only
```
- 서버에서 사용할 브로커 id
- 만약 설정하지 않으면 유니크한 브로커 id가 생성된다.
- 주키퍼가 생성한 브로커 id 와 유저가 설정한 브로커 id의 충돌을 피하기 위해 브로커 id는 `reserved.broker.max.id` 값에 + 1 되어 시작하도록 만들어진다.

---
### compression.type
```
Specify the final compression type for a given topic.
This configuration accepts the standard compression codecs ('gzip', 'snappy', 'lz4', 'zstd'). 
It additionally accepts 'uncompressed' which is equivalent to no compression; 
and 'producer' which means retain the original compression codec set by the producer.

Type:	      string
Default:	  producer
Valid Values: [uncompressed, zstd, lz4, snappy, gzip, producer]
Importance:	  high
Update Mode:  cluster-wide
```
- 주어진 토픽에 적용할 최종 압축 타입
- 이 설정은 표둔 압축 코덱 ('gzip', 'snappy', 'lz4', 'zstd')을 허용한다.
- 추가적으로 압축하지 않는다는 'uncompressed'를 허용한다;
- 그리고 프로듀서가 설정한 압축 코덱을 유지할 수 있는 'producer'를 허용한다.

---
### control.plane.listener.name
```
Name of listener used for communication between controller and brokers.
Broker will use the control.plane.listener.name to locate the endpoint in listeners list, to listen for connections from the controller. 
For example, if a broker's config is :
```
- 컨트롤러와 브로커 간 통신에 사용할 리스너 이름
- 브로커는 `control.plane.listener.name`을 사용해 리스너 리스트에서 엔드포인트를 찾아 컨트롤러와 수신한다.
- 예를 들어 만약 브로커의 설정이 아래와 같다면

```
listeners = INTERNAL://192.1.1.8:9092, EXTERNAL://10.1.1.5:9093, CONTROLLER://192.1.1.8:9094

listener.security.protocol.map = INTERNAL:PLAINTEXT, EXTERNAL:SSL, CONTROLLER:SSL

control.plane.listener.name = CONTROLLER
```
```
On startup, the broker will start listening on "192.1.1.8:9094" with security protocol "SSL".
On controller side, when it discovers a broker's published endpoints through zookeeper, it will use the control.plane.listener.name to find the endpoint, which it will use to establish connection to the broker.
For example, if the broker's published endpoints on zookeeper are :
and the controller's config is :
```
- 이 브로커는 SSL을 사용해 컨트롤러 192.1.1.8:9094와 수신을 시작한다.
- 컨트롤러는 주키퍼를 통해 브로커의 엔드포인트를 발견하면, `control.plane.listener.name` 값으로 브로커와 커넥션을 구축한다.
- 예로 카프카에 공개한 엔드포인트가 아래와 같고 
```text
"endpoints" : ["INTERNAL://broker1.example.com:9092","EXTERNAL://broker1.example.com:9093","CONTROLLER://broker1.example.com:9094"]
and the controller's config is :
```
- 컨트롤러 설정은 아래와 같다면 
```text
listener.security.protocol.map = INTERNAL:PLAINTEXT, EXTERNAL:SSL, CONTROLLER:SSL

control.plane.listener.name = CONTROLLER

listener.security.protocol.map = INTERNAL:PLAINTEXT, EXTERNAL:SSL, CONTROLLER:SSL

control.plane.listener.name = CONTROLLER
```
```
then controller will use "broker1.example.com:9094" with security protocol "SSL" to connect to the broker.
If not explicitly configured, the default value will be null and there will be no dedicated endpoints for controller connections.
If explicitly configured, the value cannot be the same as the value of `inter.broker.listener.name`.

Type:	     string
Default:	 null
Importance:	 high
Update Mode: read-only
```
- 컨트롤러는 SSL과 broker1.example.com:9094를 사용해 브로커와 연결하게 된다.
- 이 설정을 명시하지 않으면 디폴트 값은 null이고 컨트롤러 커넥션을 위한 엔드포인트를 사용하지 않는다.
- 만약 설정을 명시한다면 `inter.broker.listener.name` 값과 같을 수 없다.