# Getting Started

- https://kafka.apache.org/documentation/#gettingStarted
- `Version 3.5.0.RELEASE`

## How does Kafka work in a nutshell?

```text
Kafka is a distributed system consisting of servers and clients that communicate via a high-performance TCP network protocol.
```

- 카프카는 `TCP 프로토콜`로 통신하는 서버와 클라이언트로 구성된 분산 시스템이다.

```text
Servers: Kafka is run as a cluster of one or more servers that can span multiple datacenters or cloud regions. 
Some of these servers form the storage layer, called the brokers. 
Other servers run Kafka Connect to continuously import and export data as event streams to integrate Kafka with your existing systems such as relational databases as well as other Kafka clusters.
To let you implement mission-critical use cases, a Kafka cluster is highly scalable and fault-tolerant: if any of its servers fails, the other servers will take over their work to ensure continuous operations without any data loss.
```

- 카프카는 하나 이상의 서버를 가진 클러스터로 실행한다. 각 서버는 여러 지역에 있을 수 있다.
- 이 서버들은 `브로커` 라고 불리는 스토리지 레이어를 구성한다.
- 다른 서버들은 `카프카 커넥트`를 실행해 데이터를 끊임 없이 이벤트 스트림 방식으로 import, export 하여 다른 카프카 클러스터나 RDB 등의 기존 시스템과 통합한다.
- 카프카 클러스터는 뛰어난 확장성과 내결함성을 제공한다. 여러 서버 중 하나에 장애가 발생하더라도 다른 서버에서 데이터 손실없이 지속적인 운영을 보장한다.

```text
Clients: They allow you to write distributed applications and microservices that read, write, and process streams of events in parallel, at scale, and in a fault-tolerant manner even in the case of network problems or machine failures.
Kafka ships with some such clients included, which are augmented by dozens of clients provided by the Kafka community: clients are available for Java and Scala including the higher-level Kafka Streams library, for Go, Python, C/C++, and many other programming languages as well as REST APIs.
```

- 카프카 클라이언트는 장애가 발생하더라도 내결함성을 유지하고, 대규모 이벤트 스트림을 읽고, 처리하는 분산 어플리케이션과 마이크로 서비스를 만들 수 있다.
- 카프카는 커뮤니티에에서 클라이언트를 제공하는데, 다양한 프로그래밍 언어로 제공하며, `카프카 스트림즈 라이브러리`도 제공한다.

<br>

## Main Concepts and Terminology

```text
An event records the fact that "something happened" in the world or in your business.
It is also called record or message in the documentation.
When you read or write data to Kafka, you do this in the form of events.
Conceptually, an event has a key, value, timestamp, and optional metadata headers. Here's an example event:

Event key: "Alice"
Event value: "Made a payment of $200 to Bob"
Event timestamp: "Jun. 25, 2020 at 2:06 p.m."
```

- 이벤트는 세계 또는 비지니스 안에서 "무언가 발생했다" 는 사실을 기록한 것이다.
- 이 문서에서는 이벤트를 레코드 혹은 메세지라고 부른다.
- 카프카는 데이터 읽고 쓰기할 때 이벤트 형식으로 처리한다.
- 이벤트는 키, 값, 타임스탬프를 가지고 있고 선택적으로 메타데이터 헤더도 있을 수 있다.

```text
Producers are those client applications that publish (write) events to Kafka, and consumers are those that subscribe to (read and process) these events.
In Kafka, producers and consumers are fully decoupled and agnostic of each other, which is a key design element to achieve the high scalability that Kafka is known for.
For example, producers never need to wait for consumers. 
Kafka provides various guarantees such as the ability to process events exactly-once.
```

- `프로듀서`는 카프카에 이벤트를 발행하는 클라이언트 어플리케이션을 뜻한다.
- `컨슈머`는 이벤트를 구독하는 어플리케이션을 뜻한다.
- 프로듀서와 컨슈머는 완전히 분리되어 있으며, 각자 세부 구현을 알지 못한다.
    - 이렇게 분리함으로써 카프카에서 말하는 높은 확장성을 달성해준 핵심 설계 요소이다.
    - 예로 프로듀서는 컨슈머를 기다릴 필요가 없다.
- 카프카는 정확히 한 번만 처리하는 등의 이점을 제공한다.

```text
Events are organized and durably stored in topics. 
Very simplified, a topic is similar to a folder in a filesystem, and the events are the files in that folder. 
Topics in Kafka are always multi-producer and multi-subscriber: a topic can have zero, one, or many producers that write events to it, as well as zero, one, or many consumers that subscribe to these events. 
Events in a topic can be read as often as needed—unlike traditional messaging systems, events are not deleted after consumption.
Instead, you define for how long Kafka should retain your events through a per-topic configuration setting, after which old events will be discarded. 
Kafka's performance is effectively constant with respect to data size, so storing data for a long time is perfectly fine.
```

- 이벤트는 토픽에서 저장되어 안전하게 저장된다. 토픽은 파일 시스템의 폴더와 유사하며, 이벤트는 폴더 안에 파일로 볼 수 있다.
- 카프카의 토픽은 언제나 멀티 프로듀서와 멀티 컨슈머가 접근할 수 있다.
- 토픽의 이벤트는 다시 읽을 수 있다. 기존 메세징 시스템과 달리 이벤트를 소비하면 삭제하지 않는다.
- 대신에, 토픽별로 이벤트 보존 기간을 정의할 수 있으며, 오래된 이벤트를 폐기할 수 있다.
- 카프카의 성능은 데이터의 크기가 크더라도 효과적으로 일정하므로 데이터를 장기간 저장해도 괜찮다.

```text
Topics are partitioned, meaning a topic is spread over a number of "buckets" located on different Kafka brokers. 
This distributed placement of your data is very important for scalability because it allows client applications to both read and write the data from/to many brokers at the same time.
When a new event is published to a topic, it is actually appended to one of the topic's partitions. 
Events with the same event key (e.g., a customer or vehicle ID) are written to the same partition, 
and Kafka guarantees that any consumer of a given topic-partition will always read that partition's events in exactly the same order as they were written.
```

- 토픽은 파티셔닝 된다. 즉, 토픽은 서로 다른 카프카 브로커에 있는 여러 버킷? 으로 분산된다.
- 데이터를 분산해서 저장하는 이유는 확장성을 위해서다. 왜냐하면 클라이언트 어플리케이션은 동시에 여러 브로커에서 데이터를 읽기/쓰기를 할 수 있다.
- 토픽에 새로운 이벤트가 발행되면, 토픽의 파티션 중 하나에 추가된다.
- 이벤트 키가 동일한 이벤트는 같은 파티션에 기록되며,
- 하나의 토픽-파티션을 소비하는 모든 컨슈머는 기록한 순서와 같은 순서로 파티션에 있는 이벤트를 정확히 읽어가는걸 보장한다.

```text
To make your data fault-tolerant and highly-available, every topic can be replicated, even across geo-regions or datacenters, so that there are always multiple brokers that have a copy of the data just in case things go wrong, you want to do maintenance on the brokers, and so on. 
A common production setting is a replication factor of 3, i.e., there will always be three copies of your data. 
This replication is performed at the level of topic-partitions.
```

- 내결함성, 고가용성 데이터를 만들기 위해 모든 토픽은 복제할 수 있다. 지리적으로 분산시키거나 여러 데이터 센터에 복제할 수 있다.
- 혹시 모를 문제를 대비하여 브로커 관리를 위해 항상 데이터 복제본을 가진 멀티 브로커가 존재하게 된다.
- 보통 프로덕션 세팅으로 replication factor 설정을 3으로 둔다.
    - 항상 데이터 복제본이 3개 존재하게 된다.
- 데이터 복제는 토픽-파티션 레벨에서 이루어진다.


## Kafka APIs

```text
The Admin API to manage and inspect topics, brokers, and other Kafka objects.
The Producer API to publish (write) a stream of events to one or more Kafka topics.
The Consumer API to subscribe to (read) one or more topics and to process the stream of events produced to them.
The Kafka Streams API to implement stream processing applications and microservices. It provides higher-level functions to process event streams, including transformations, stateful operations like aggregations and joins, windowing, processing based on event-time, and more. Input is read from one or more topics in order to generate output to one or more topics, effectively transforming the input streams to output streams.
The Kafka Connect API to build and run reusable data import/export connectors that consume (read) or produce (write) streams of events from and to external systems and applications so they can integrate with Kafka. For example, a connector to a relational database like PostgreSQL might capture every change to a set of tables. However, in practice, you typically don't need to implement your own connectors because the Kafka community already provides hundreds of ready-to-use connectors.
```

- 어드민 API : 토픽, 브로커, 카프카 오브젝트들을 관리할 수 있는 API
- 프로듀서 API : 카프카 토픽에 이벤트 스트림을 발행할 수 있는 API
- 컨슈머 API : 하나 이상의 토픽을 구독하고 토픽에 생성한 이벤트 스트림즈를 처리할 수 있는 컨슈머 API
- 스트림즈 API : 데이터 변환이나 집계나 조인, 이벤트 시간 기반 처리 등 고급 기능을 제공한다. 하나 이상의 토픽에서 읽은 데이터를 입력으로 받아 하나 이상의 토픽에 쓸 출력을 만들 수 있다.
- 커넥터 API : 커넥터는 외부 시스템과 어플리케이션간의 이벤트 스트림을 read 하고 write할 수 있기 때문에 외부 시스템과 카프카를 통합할 수 있다.
    - 예로 PostgreSQL과 같은 관계형 데이터베이스를 연결해주는 커넥터는 테이블에 생기는 모든 변경 사항을 잡아낼 수 있다.
    - 카프카 커뮤니케이션에는 이미 수백개 이상의 커넥터가 존재하여 자체 구현할 일은 없다.

## Messaging

```text
Kafka works well as a replacement for a more traditional message broker. 
In comparison to most messaging systems Kafka has better throughput, built-in partitioning, replication, and fault-tolerance which makes it a good solution for large scale message processing applications.
In our experience messaging uses are often comparatively low-throughput, but may require low end-to-end latency and often depend on the strong durability guarantees Kafka provides.
```

- 카프카는 다른 전통적인 메시지 큐 제품들로 대체할 수 있다.
- 카프카를 다른 메세징 시스템과 비교해보면, 더 나은 처리량을 보장해주고, 파티셔닝 기능이 내장되어 있으며, 복제와 내결함성을 제공하므로, 대규모 메시지 처리 솔루션으로 적합하다.
- 경험상 메세징 시스템은 비교적 처리량이 낮더라도 end to end 지연 시간이 짧아야 하며, 카프카가 보장하는 강력한 내구성이 필요하다.