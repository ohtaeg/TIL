# Transaction
- 다수의 명령을 하나의 트랜잭션으로 처리, 원자성 보장
- 중간에 에러가 발생하면 모든 작업 rollback
- 하나의 트랜잭션이 처리되는 동안 다른 클라이언트의 요청이 중간에 끼어들 수 없다.
- 파이프라인과 트랜잭션은 다른 기술이지만 서로 동시에 사용할 수도 있다.

```redis
-- 트랜잭션 시작
local> $ MULTI
OK

-- 트랜잭션이 시작되고 수행할 명령어들 입력
-- ex) foo 에 값 증가
local(TX)> $ INCR foo 
QUEUED

local(TX)> $ GET foo
nil 

-- 트랜잭션 롤백
local(TX)> $ DISCARD 
nil

local> $ MULTI
OK

local(TX)> $ INCR foo 
QUEUED

-- 트랜잭션 커밋
local(TX)> $ EXEC
(integer) 1

local> $ GET foo
1
```