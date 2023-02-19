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

- 디스크 공간 관련해서 [공식 문서](https://www.elastic.co/guide/en/elasticsearch/reference/current/modules-cluster.html#disk-based-shard-allocation)의 내용들을 찾아보니
```text
Controls the low watermark for disk usage.
It defaults to 85%, meaning that Elasticsearch will not allocate shards to nodes that have more than 85% disk used.
```
- **기본 85% 이상 Disk를 사용하면 shard allocation 이 안된다.**

<br>

### 그렇다면 디스크 용량 부족시 대처 방법을 어떻게 했는가?

- 애초에 디스크 용량이 너무 작았다. 디스크 용량을 증설해버림
- 파티션 현황을 확인

```shell
$ fdisk -l


Disk /dev/loop0: 46.98 MiB, 49233920 bytes, 96160 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes

...

Disk /dev/nvme0n1: 100 GiB, 107374182400 bytes, 209715200 sectors
Disk model: Amazon Elastic Block Store              
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 4096 bytes / 4096 bytes
Disklabel type: dos
Disk identifier: 0x24ca9e81

Device         Boot Start      End  Sectors Size Id Type
/dev/nvme0n1p1 *     2048 16777182 16775135   8G 83 Linux
```
- 위와 같이 여러 loop 파티션이 나눠져있는데, 증설된 EBS `/dev/nvme0n1` 을 찾을 수 있었고
- 부팅 볼륨 `/dev/nvme0n1p1` 을 찾을 수 있다.
- 해당 볼륨이 우분투에 마운트가 Attach 되었는지 확인한다.
```shell
$ lsbik

NAME        MAJ:MIN RM  SIZE RO TYPE MOUNTPOINT
loop0         7:0    0   47M  1 loop /snap/snapd/16010
...
nvme0n1     259:0    0  100G  0 disk 
└─nvme0n1p1 259:1    0    8G  0 part /
```


### 엘라스틱서치 배열 조회시 조심해야할 점
- 엘라스틱서치에서 만약 SQL 쿼리로 배열 데이터 조회시 다음과 같은 에러를 만날 수 있다.

```sql
{
  "error" : {
    "root_cause" : [
      {
        "type" : "ql_illegal_argument_exception",
        "reason" : "Arrays (returned by [host.ip]) are not supported"
      }
    ],
    "type" : "ql_illegal_argument_exception",
    "reason" : "Arrays (returned by [host.ip]) are not supported"
  },
  "status" : 500
}
```

- [공홈](https://www.elastic.co/guide/en/elasticsearch/reference/current/sql-limitations.html#_array_type_of_fields)에서 다음과 같이 설명되어있다.
```text
Array fields are not supported due to the "invisible" way in which Elasticsearch handles an array of values: 
the mapping doesn’t indicate whether a field is an array (has multiple values) or not, so without reading all the data, Elasticsearch SQL cannot know whether a field is a single or multi value. 
When multiple values are returned for a field, by default, Elasticsearch SQL will throw an exception. 
However, it is possible to change this behavior through field_multi_value_leniency parameter in REST (disabled by default) 
or field.multi.value.leniency in drivers (enabled by default).
```
- 길게 설명되어있지만 요약하면 **지원하지 않는다**고 한다.