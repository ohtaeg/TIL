# 메모리

메모리 한계상 메모리 용량 보다 데이터를 더 많이 사용하게 되면, 메모리 이슈가 발생할 수 있다.

많은 데이터를 가지고 있는 레디스는 메모리를 어떻게 관리할까?


## max memory 옵션
- Redis는 `maxmemory` 옵션을 통해 메모리양을 해당 값 이상으로 사용하지 않도록 제한할 수 있다.
- 현재 아무것도 세팅안한 로컬 레디스에 max-memory 값은 몇일까?

```redis
$ redis-cli
127.0.0.1:6379> config get maxmemory
1) "maxmemory"
2) "0"
```

**0이다.** [공식문서](https://redis.io/docs/latest/develop/reference/eviction/)에 따르면 **64bit 환경인 경우 기본 값을 0으로 세팅하며 메모리 제한을 두지 않는다.**

### 그렇다면 메모리 제한없이 사용하도록 하면 좋은걸까?
지금은 [deprecated](https://redis.io/docs/latest/operate/oss_and_stack/reference/internals/internals-vm/) 되었지만 maxmemory 옵션으로 제한을 두지 않으면 Virtual memory를 사용하여 swap하게되어 성능 저하가 있다고 한다.

> Redis will not use any more memory than maxmemory allows.
If you turn maxmemory off, Redis will start using virtual memory (i.e. swap), and performance will drop tremendously.

즉, swap을 하게 되면 디스크의 공간을 메모리처럼 사용하게 되어 처리 속도가 떨어져 성능 저하로 이어질 수 있다.

혹은 메모리보다 더 많은 데이터를 사용한 경우 OOM이 발생할 수 있다.

시스템의 메모리 한계를 인식하지 못해 더 많은 메모리를 요구하여 문제가 발생할 수 있기 때문에 따로 설정을 해주어야 한다.

<br>

### 그렇다면 max memory 값을 몇으로 해야하는가?
Redis 공식 문서나 권장 사항에서 특정 비율로 메모리를 할당하는 것에 대한 명시적인 지침을 제공하지는 않지만

많은 운영 가이드와 베스트 프랙티스 자료에서는 서버의 전체 메모리 중 일부를 Redis에 할당하고 나머지는 운영 체제와 다른 프로세스를 위해 남겨두는 것이 일반적이라고 권장하며,

**가능한 한 30% 정도 여유 메모리를 갖도록 서버를 운영해야한다.**

즉, 전체 메모리의 60~70% 사용을 권장한다. ex) 시스템 메모리가 16gb면 Redis 서버가 최대 12gb정도 사용하도록

<br>

### 만약 maxmemory 설정한 만큼 메모리를 사용하고 있다고 가정할 때, 데이터들을 추가해 max memory를 넘긴다면?
### 만약 논리적으로 사용되고 있는 메모리의 크기가 maxmemory보다 커진다면?

시스템 메모리 크기는 제한적일 수 밖에 없고 상대적으로 사용자 데이터는 이보다 훨씬 더 클 수 밖에 없기 때문에 모든 데이터를 메모리에서 관리할 수 없다.

[redis-blog](https://redis.io/kb/doc/1jbxid5qq7/is-maxmemory-the-maximum-value-of-used-memory)에 따르면 메모리 정책이 설정되어 있는 경우, 새로운 데이터가 들어오게 되면 설정에 따라 기존 데이터가 제거되는 방식이다.

<br>

## Max Memory Policy
Redis는 설정 파일이나 명령을 통해 사용할 최대 메모리 양을 설정할 수 있다. 

설정을 통해 지정된 메모리 양을 초과하지 않도록 보장한다.

메모리 한도에 도달하면, Redis는 설정에 따라 메모리 해제 정책을 적용하며 해제 정책에는 여러가지 값들이 있으며 4.0 버전부터 아래와 같은 알고리즘을 제공한다.
- LRU (Least Recently Used: 가장 최근에 사용되지 않은 것) 
- LFU (Least-Frequently-Used: 가장 적게 사용된 것) 

<br>

### Eviction Policies
- `noeviction`
  - 데이터를 지우지 않고 메모리가 maxmemory 이상을 사용하게 되면 error를 발생
- `allkeys-lru`
  - 모든 키를 대상으로 가장 최근에 사용하지 않는 키(LRU)를 먼저 퇴거(eviction)하여 새로운 데이터를 위한 공간을 확보
- `allkeys-lfu`
  - 모든 키를 대상으로 가장 적게 사용된 키(LFU)를 먼저 퇴거(eviction)하여 새로운 데이터를 위한 공간을 확보
- `allkey-random`
  - 모든 키를 대상으로 랜덤으로 삭제

<br>

- `volatile-lru`
  - 만료 설정된 키 중 가장 최근에 사용하지 않는 키(LRU)를 삭제한다.
- `volatile-lfu`
  - 만료 설정된 키 중 가장 적게 사용된 키(LFU)를 삭제한다.
- `volatile-random`
  - 만료 설정된 키 중 랜덤으로 삭제한다.

<br>


## 레플리카 환경에서는 max memory 설정을 주의해야한다.
redis 5.0 부터, 기본적으로 replica들은 maxmemory 설정을 무시한다.

마스터에서 슬레이브로 del 명령으롤 통해 해당 옵션을 제거하고 마스터 값으로 일관성을 유지한다.

write 가 master에서 처리되고 slave로 전달되는 구조다보니 master와 slave 노드가 서로 다른 maxmemory 값을 갖고 있으면 문제가 될 여지가 있다.

replica-ignore-maxmemory 옵션으로 제어할 수 있는데 기본 값으로 yes이다.

## REF
- https://redis.io/docs/latest/develop/reference/eviction
- https://redis.io/kb/doc/1jbxid5qq7/is-maxmemory-the-maximum-value-of-used-memory
- https://redis.io/docs/manual/replication
- https://redis.io/docs/latest/operate/oss_and_stack/reference/internals/internals-vm/