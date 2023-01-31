# 트러블 슈팅
### Unavailable_shard_exception - Primary shard is not active timeout
- 회사에서 개발 로그 서버가 죽은적이 있다. 이에 대한 트러블 슈팅을 기록해보았다.
- 로그 서버는 logstash + es가 같이 설치되어있으며 ubuntu 20.0.4 환경이다.
- 로그가 수집되지 않아 확인해보니 running 중이던 logstash가 중단되었다.
- /var/log/logstash 경로를 가서 최신 날짜의 로그를 확인해보니 찍힌 에러는 다음과 같다.

```shell
$ tail -n 50 logstash-plain-2022-12-19-6.log
```
```json
{
  "type":"unavailable_shards_exception",
  "reason":"primary shard is not active Timeout: [1m], request: [BulkShardRequest [0] containing [125] requests]"
}
```

- unavailable_shards_exception - primary shard is not active Timeout
- 이 에러는 무엇인가?

<br>

> Primary shard is not active timeout

- **1차 샤드가 활성화되지 않는다는 오류**
- 1차 샤드 == 프라이머리 샤드(처음 생성된 샤드)
- 인덱스 생성시 별도의 설정을 하지 않으면 7.0 버전 부터는 디폴트로 1개의 샤드로 인덱스가 구성된다.

<br>

- 개인적으로 별도의 설정을 하지 않아 1개의 샤드만 생성될텐데 해당 샤드가 만들어지지 않았다.
- 엘라스틱서치는 새로운 샤드를 어디에 할당할지 결정하기전에 노드의 사용 가능한 디스크 공간을 고려한다.
  - 즉 현재 상황은 인덱스당 샤드 1개이기 떄문에 새로운 인덱스 생성시 디스크 공간을 고려한다.
- 즉, 디스크 공간이 부족하다는 추측을 할 수 있었다.

<br>

- 디스크 공간 관련해서 [공식 문서](https://www.elastic.co/guide/en/elasticsearch/reference/current/modules-cluster.html#disk-based-shard-allocation)의 내용들을 찾아보니
```text
Controls the low watermark for disk usage.
It defaults to 85%, meaning that Elasticsearch will not allocate shards to nodes that have more than 85% disk used.
```
- **기본 85% 이상 Disk를 사용하면 shard allocation 이 안된다.**


