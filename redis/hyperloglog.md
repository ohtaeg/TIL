# HyperLogLog
- 집합의 카디널리티를 추정할 수 있는 확률형 자료구조
  - 집합의 원소 갯수를 추정
  - 확률형이다보니 실제 결과와 오차가 있을 수 있다. 매우 큰 데이터의 오차가 1% 이하의 근사치를 구할 때 사용
  - **정확성을 일부 포기하는 대신 저장공간을 효율적으로 사용**
  - ex) 검색 엔진의 하루 검색어 수 계산
- **메모리는 적게 사용**하고 오차가 적은편
  - Set을 사용할 경우 값을 메모리에 저장하여 원소의 수에 따라 메모리를 사용한다.
  - 예로 Redis Set에 1백만개의 숫자를 저장하면 4,848kb, 1천만개의 숫자를 저장하면 46,387kb를 사용
  - HyperLogLog를 사용하면 실제 값을 저장하지 않기 때문에 원소 개수와 상관없이 고정으로 12kb만 사용
  - 16384개 레지스터를 사용하고 레지스터 당 6bits를 사용
    - 16,384 * 6bits = 98,304bits / 8 = 12,288bytes
- 값을 해싱하여 bucket 단위로 분류하여 관리
  - 해시충돌이 발생하는 경우 정확하지 않을 수 있다.
- 실제 값을 저장하지 않고 해싱해서 관리하기 때문에 출력해야하는 경우 활용할 수 없다.


## PFADD
- 멤버 추가 명령어
- 원소를 추가했을 경우 원소의 개수와 상관없이 1을 리턴, 원소가 이미 집합에 있을 경우는 0을 리턴합니다.

> PFADD key element [element ...]

```redis
$ PFADD fruits apple orange kiwi grape
(integer) 1
```

## PFCOUNT
- 멤버 갯수 조회 명령어
- 추정 오류는 평균 0.81%

> PFCOUNT key

```redis
$ PFCOUNT fruits
(integer) 4
```

## vs SET
- bash 커맨드로 루프를 통해 redis-cli sadd 명령어로 Set에 k1라는 key에 값들을 천개 넣는다.

```shell
$ for (( i=1; i<=1000; i++)); do redis-cli SADD k1 $i; done
```

- 메모리 확인을 해보면 48304 byte 출력
- 카디널리티 출력해보면 1000
```redis
$ MEMORY USAGE k1
(integer) 48304

$ SCARD k1
(integer) 1000
```

<br>

- 위와 동일한 방법으로 하이퍼로그로그에 k2라는 키에 넣어보자.
```shell
$ for (( i=1; i<=1000; i++)); do redis-cli PFADD k2 $i; done
```

- 메모리 확인을 해보면 2616 byte 출력, set에 비해 20배 이상 매우 적은 메모리만 사용
- 카디널리티 출력해보면 1001개 출력, 오차 발생
```redis
$ MEMORY USAGE k2
(integer) 2616

$ PFCOUNT k2
(integer) 1001
```