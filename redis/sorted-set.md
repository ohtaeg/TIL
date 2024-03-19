# Sorted Set
- 중복없이 String value를 저장하는 set이면서 `Score`라는 추가적인 Field를 가짐으로써 Score를 통해 데이터를 정렬 가능한 타입
- 값을 추가하는 순간에 `Score`에 의해 정렬을 유지
- 만약 `Score 가 동일하다면 사전 순으로 정렬된다.
- 내부적으로 `Skip List` + `Hash table`로 이루어져 있다.
- Sorted Set은 줄여서 `ZSet`이라고도 불린다.


## ZADD
- ZADD ${key} ${score} ${member} ${score} ${member}..
- 특정 key에 zset을 생성하면서 각 member에 score를 부여
- score는 무조건 숫자만 입력이 가능하다.
```redis
$ ZADD points 10 B 10 A 50 C
-- B 와 A는 score가 같지만 사전 순 정렬로 인해 A가 먼저 온다.
-- 1) A
-- 2) B
-- 3) C

$ ZADD points 40 C
-- member가 존재한다면 update
```

<br>

- **ZADD ${key} [NX | XX] [GT | LT] [CH] [INCR] ${score} ${member}..**
- 여러 옵션을 줄 수 있다.
- `NX` : 이미 존재하는 member에 대해서 업데이트하지 않고 없을 경우에만 추가
- `XX` : 이미 존재하는 member에 대해서 업데이트
- `GT` : 이미 존재하는 member의 Score보다 크면 업데이트
- `LT` : 이미 존재하는 member의 Score보다 작으면 업데이트
```redis
$ ZADD points 10 B 10 A 50 C

$ ZADD points NX 50 C
-- (integer) 0

$ ZADD points NX 20 D
-- (integer) 1


$ ZADD points XX 30 E
-- (integer) 0

$ ZADD points XX 30 D
-- (integer) 1
```


## ZRANGE
- zset은 순서를 가지기 때문에 리스트와 마찬가지로 `RANGE` 명령으로 인덱스를 통해 범위 조회 가능
```redis

$ ZADD points 10 B 10 A 50 C

--    [0]  [1]  [2]
--   [-3] [-2] [-1]
--   [ A    B    C ]
$ ZRANGE points 0 -1
-- 1) A
-- 2) B
-- 3) C
```

<br>

- with score 옵션을 주면 스코어와 함께 반환한다.
- reverse with score 옵션을 주면 역순으로 스코어에 함께 반환한다.
```redis

$ ZADD points 10 B 10 A 50 C

$ ZRANGE points 0 -1 WITHSCORES
-- 1) "A"
-- 2) "10"
-- 3) "B"
-- 4) "10"
-- 5) "C"
-- 6) "50"




--    [0]  [1]  [2]
--   [-3] [-2] [-1]
--   [ A    B    C ]
$ ZRANGE points 0 -1 REV WITHSCORES
-- 1) "C"
-- 2) "50"
-- 3) "B"
-- 4) "10"
-- 5) "A"
-- 6) "10"
```

## ZRANK
- ZSET에서 지정된 member의 순위를 반환
- 랭킹은 0부터 시작하는 인덱스 값과 동일하다.
```redis
$ ZRANK points A
-- 0

$ ZRANK points C
-- 2
```

