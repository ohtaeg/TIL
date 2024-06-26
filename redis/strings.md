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

### OTP
- One Time Passowrd 인증을 위해 사용되는 임시 비밀번호로 활용할 수 있다.
1. 유저가 서비스에 인증을 위해 휴대폰 번호를 입력한다.
2. 입력한 휴대폰번호가 해당 유저의 휴대폰인지 확인하기 위해 6자리의 짧은 코드를 레디스에 저장
   - SET 010-1234-5678:otp 123456 EX 180
3. 6자리 코드 전송, 유저가 입력한 코드가 같으면 인증 성공 
   - GET 010-1234-5678:otp

### 분산락
- 분산환경에서 다수의 프로세스에서 동일한 자원에 접근할 때, 동시성 문제 해결
- 해당 공유 자원을 표현할 수 있는 이름을 통해 락의 키를 결정하고 nx 옵션을 통해 키가 존재하지 않는 경우 락을 획득할 수 있도록 조정
- 이미 존재하여 락을 획득할 수 없는 경우 nil을 리턴하여 프로세스를 대기시키거나 예외를 발생시키는 등 상황에 따른 뒷처리

### Rate Limiter
- 시스템 안정성을 위해 특정 IP 별로 초당 요청할 수 있는 요청수를 제한하거나 
- 특정 유저별로 횟수를 제한, 특정 어플리케이션 별로 요청 제한을 가능

<br>

RateLimiter의 횟수를 세는 기준에 따라 여러가지 구현 방법이 있는데 그중 하나는 아래와 같다.
- Fixed window Rate Limiting
- 고정된 시간 안에 요청수를 제한하는 방법
- Timeline을 고정된 간격의 window로 나누고, 각 window마다 count를 세팅한다.
- 요청이 접수될 때 마다 카운터의 값을 1씩 증가 시킵니다.
- 카운터의 값이 사전에 설정된 limit에 도달하면, 새로운 요청은 새 윈도우가 열릴때까지 실패 처리
  1. 사용자가 10분에 요청
  2. GET 1.1.1.1:10  (사용자 ip:시간)
  3. 콜이 초과했으면 서버에서 429 에러 응답
  4. 초과하지않았으면 트랜잭션을 열어 카운트 증가 MULTI / INCR 1.1.1.1:10

- window 경계 부근에 순간적으로 많은 트래픽이 집중될 경우 window에 할당된 양보다 더 많은 요청이 처리될 수 있는 단점이 있다.
