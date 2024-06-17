# hash
- hash key - field - value 구조를 갖는 데이터 타입
- field는 해당 hash key의 sub key이다.
  - key 하위에 field가 저장되는 구조
- 특정 object에서 하나의 속성만 빠르게 조회하고 싶은 경우, JSON string으로 저장하고 읽는 것이 비효율적일 수 있다.

### HSET
- 입력한 hash key 밑에 지정한 field 에 value를 저장하는 명령어
- hash key 밑에 여러 필드에 값을 저장할 수 있다.
- 필드 값이 존재하면 덮어쓴다.

```redis
-- ohtaeg 라는 key에 name 필드에는 이름을, status 필드에는 기분 상태를
$ HSET ohtaeg name ohtaegyeong status soso
```

### HGET
- 입력한 key 에 지정한 필드 값을 가져온다
- `HGET`은 O(1)의 시간 복잡도를 갖는다.
- 존재하지 않는 key를 조회시 nil 반환
```redis
-- ohtaegyeong
$ HGET ohtaeg name
```


### HMGET
- H-MULTI-GET은 다수의 필드를 조회하는 명령어
- 존재하지 않는 key를 조회시 nil 반환
```redis
-- 1) ohtaegyeong
-- 2) soso
$ HGET ohtaeg name status
```
### HINCRBY
- 숫자형으로 저장된 field에 특정 값만큼 더하는 명령어
```redis
$ HSET ohtaeg age 32

-- 33
$ HINCRBY ohtaeg age 1
```

<br>

- 숫지형이 아닌 필드에 `HINCRBY` 하는 경우 타입 에러가 발생
```redis

-- (error) ERR hash value is not an integer
$ HINCRBY ohtaeg name 1
```

<br>

## 로그인 세션
- 로그인 상태를 유지하기 위한 기술, 서버에서는 유저를 식별하기 위해 세션 id를 사용하고 해당 세션 id로 인증없이 사용자 식별
- 로그인시 동시 로그인 제한에도 사용, 새로운 사용자가 로그인 요청을 하면 유효한 세션의 갯수를 확인하여 동시에 로그인 가능한 디바이스 갯수 제한

1. login request
2. HSET abcd id ohtaeg grade premium
3. Set-cookie response (sessionId: abcd)
4. 

