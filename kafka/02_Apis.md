# Apis

- https://kafka.apache.org/documentation/#api
- `Version 3.5.0.RELEASE`

## Apis
- 카프카는 5개의 코어 Api를 제공한다.
```text
1. The Producer API allows applications to send streams of data to topics in the Kafka cluster.
2. The Consumer API allows applications to read streams of data from topics in the Kafka cluster.
3. The Streams API allows transforming streams of data from input topics to output topics.
4. The Connect API allows implementing connectors that continually pull from some source system or application into Kafka or push from Kafka into some sink system or application.
5. The Admin API allows managing and inspecting topics, brokers, and other Kafka objects.
```
1. 프로듀서 API는 어플리케이션이 카프카 클러스터 안에 있는 토픽에 데이터 스트림을 전송하는 것을 허용해주는 API이다.
2. 컨슈머 API는 어플리케이션이 카프카 클러스터 안에 있는 토픽에 데이터 스트림을 읽어가는 것을 허용해주는 API이다.
3. 스트림즈 API는 입력 토픽으로부터 출력 토픽으로 데이터 변환을 허용햐주는 API이다.
4. 커넥트 AP는 지속적으로 소스 시스템에서 데이터를 pull 하여 카프카로 보내거나 카프카 데이터를 다른 싱크 시스템 or 어플레키에션으로 push하는 커넥터를 구현한 API이다.
5. 어드민 API는 토픽, 브로커, 다른 카프카 오브젝트를 관리하고 점검하는 API이다.

### Producer API
- 어플리케이션에서 프로듀서 API를 사용하면 카프카 클러스터에 있는 토픽으로 데이터를 전송할 수 있다.
- 프로듀서를 사용하려면 아래와 같이 메이븐 의존성을 추가하라.
```xml
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-clients</artifactId>
	<version>3.5.0</version>
</dependency>
```

### Consumer API
- 어플리케이션에서 프로듀서 API를 사용하면 카프카 클러스터에 있는 토픽에서 데이터를 읽어갈 수 있다.
- 컨슈머를 사용하려면 아래와 같이 메이븐 의존성을 추가하라.
```xml
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-clients</artifactId>
	<version>3.5.0</version>
</dependency>
```

### Streams API
- 스트림즈 API를 사용하면, 입력 토픽 데이터를 읽어 출력 토픽으로 전송하는 데이터 변환 스트림을 구성할 수 있다.
- 스트림즈 API 사용법에 대한 추가 설명은 [여기](https://kafka.apache.org/35/documentation/streams/)를 이용해라.
- 카프카 스트림즈를 사용하려면 아래와 같이 메이븐 의존성을 추가하라.
```xml
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-streams</artifactId>
	<version>3.5.0</version>
</dependency>
```

### Connect API
```text
Many users of Connect won't need to use this API directly, though, they can use pre-built connectors without needing to write any code. 
```
- 많은 커넥트 유저들은 이 API를 직접 사용할 필요가 없고, 미리 만들어준 커넥터를 사용하면 된다. 
- 커넥트 사용법에 대한 추가 정보는 [여기](https://kafka.apache.org/documentation.html#connect)에서 확인할 수 있다.

### Admin API
- 어드민 API를 사용하려면 아래와 같은 메이븐 의존성을 추가해라.
```xml
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-clients</artifactId>
	<version>3.5.0</version>
</dependency>
```