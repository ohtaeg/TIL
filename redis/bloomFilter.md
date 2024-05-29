# BloomFilter
- element가 특정 집합에 포함되었는지 확인할 수 있는 확률형 자료구조
- 정확성을 일부 포기하는 대신 저장공간을 효율적으로 사용
  - 실제 값을 저장하지 않아 적은 메모리 사용
- false nagative는 없으나 `false positive가 발생할 수 있다.`
  - 값이 집합에 실제로 포함되지 않았는데 포함되었다고 잘못 말하는 경우가 있음 (false positive)
  - 값이 집합에 실제로 포함되어있는데 포함되지 않았다고 하는 경우는 없다. (false nagative)
- 값을 해싱하여 여러 개의 해시키를 만들어 블룸필터 자료구조 내에 표시를 한다.
  - 이후 어떤 값이 존재하는지 확인할 때 해시키를 생성하여 해당 위치를 확인하는 원리
  - 서로 다른 값인데 해시 값이 같아 해시 충돌이 발생하게 되면 이때 false positive가 발생할 수 있다.
- Chrome은 악성 URL을 확인하기위해 사용.


## 블룸필터 설치
- Redis에서 BloomFilter는 기본적으로 내장된 자료구조가 아니기 때문에 별도로 모듈을 설치하거나 도커를 이용해야한다.
- Redis Stack Server 이미지 안에 블룸 필터가 포함되어있어 도커로 진행
```shell
$ docker run -p 63790:6379 -d --rm redis/redis-stack-server
$ redis-cli -p 63790
```


## BF.ADD
- 블룸필터에 값 추가 명령어
- 여러 값을 넣으려면 MADD 활용

> BF.MADD key element

```redis
$ BF.MADD fruits apple orange grape
1) (integer) 1
2) (integer) 1
3) (integer) 1
```


## BF.EXISTS
- 블룸필터에 값이 존재하는지 확인

> BF.EXISTS key element

```redis
$ BF.EXISTS fruits apple
(integer) 1

$ BF.EXISTS fruits banana
(integer) 0
```

- 데이터가 많은 경우 바나나가 없는데도 있다는 false positive가 있을 수 있음