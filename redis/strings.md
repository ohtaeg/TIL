# Strings type

### $ SET, MSET, MGET
- 레디스의 기본적인 데이터 타입
- 문자열, 숫자, json string 등 저장
- 레디스는 데이터 타입에 따라 명령어가 다른데 String 타입은 `SET` 을 이용한다.
- `MSET` 은 멀티셋의 약자로써, 다수의 String 값을 한번에 저장하는 명령어
- `MGET` 은 다수의 키를 통해 값을 한번에 반환하는 명령어


```redis
$ SET maeil sunup

-- price key에 100 값을, lang key에 ko 값을
$ MSET price 100 lang ko
$ MGET price lang
```

<br>

### $ INCR, INCRBY
- 레디스는 정수 타입 없이 숫자도 String으로 저장하는 대신 `INCR` 은 Increase의 약자로 숫자로된 스트링 값을 1 증가시킬 수 있다.
- `INCRBY` 는 숫자로된 스트링 값에 특정 값을 더할 때 사용된다.
```redis
$ INCR price
$ INCRBY price 10
```

<br>

### Json String
- 레디스에는 key-value 형태의 Json String을 저장할 수 있다.
- 대신 저장한 후 사용할 때는 직접 JSON으로 바꿔서 사용해야한다.

```redis
$ SET jsonstring '{"price": 100, "lang": "ko"}'

$ GET jsonstring
> "{\"price\":100, \"lang\":\"ko\"}"
```

<br>

### key 네이밍 컨벤션
- 레디스는 일반적으로 키를 만들 때 `콜론 (:)`을 이용하여 구분한다.
- 한국어로 가격이 100원이다 라는 데이터를 저장하는 경우
```redis
$ SET ko:price 100
```