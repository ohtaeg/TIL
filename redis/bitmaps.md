# Bitmaps
- 실제 데이터 타입이 아닌, String에 binary 표현
  - 이때 1:1 관계가 된다.
- 최대 42억개 binary 데이터 표현가능
- 적은 메모리를 사용하여 바이너리 상태값을 저장하는데 많이 활용
  - 출석부, 로그인 여부 등등

## SETBIT

> SETBIT key offset value

- offset : 0부터 시작
- value : 0, 1만 사용 가능, 기본값은 0이다.

```redis
$ SETBIT user:login:24-01-01 123 1
(integer) 0
$ SETBIT user:login:24-01-01 456 1
(integer) 0
$ SETBIT user:login:24-01-02 123 0
(integer) 1
```

## BITCOUNT
- value 값이 1인 bit 수를 카운팅

> BITCOUNT key [start end]

```redis
$ BITCOUNT user:login:24-01-01
(integer) 2
```


## BITOP
- bit operation으로, AND OR XOR NOT 연산이 가능하다.

> BITOP operation destkey key [key ...]

- destkey : 바로 결과값을 출력하지 않고 해당 연산의 결과를 다른 비트맵으로 생성하기 때문에 결과값을 담을 새로운 키**

```redis
$ BITOP AND result user:login:24-01-01 user:login:24-01-02
(integer) 58
```
