# 격리 수준

### Goal

Isolation level을 정리할수 있다.

Isolation level과 lock 연관성을 알 수 있다.


---

# Row-level Lock
**기본적으로 SELECT 문을 통해 조회한 데이터는 Non-Locking Read 이다.**

MySQL에서 두가지 Locking read 를 제공한다.

- SELECT ~ `FOR SHARE`
    - 다른 트랜잭션이 Row 를 읽을 순 있지만, `S-Lock` 을 설정한 트랜잭션이 커밋되기 전까지 Row 를 수정할 수 없다.
- SELECT ~ `FOR UPDATE`
    - UPDATE는 UPDATE문을 실행한 것처럼 읽을 때도 동일한 ROW를 반환하겠다는 뜻으로써
      읽기 및 쓰기에 `X-Lock`을 설정

<br>

## Shared Lock (S Lock)
- 읽기 락으로도 불린다. Row를 읽을때 사용되어지는 Lock
- 다른 트랜잭션이 Row 를 읽을 순 있지만, `S-Lock` 을 설정한 트랜잭션이 커밋되기 전까지 다른 트랜잭션이 쓰기 연산을 할 수 없다.
- 공유 락이 걸린 데이터에 대해서 다른 트랜잭션도 똑같이 공유 락을 획득할 수 있으나, 배타 락은 획득할 수 없다.
  - 여러 트랜잭션이 동시에 한 Row에 Shared Lock을 걸 수 있다. 
  - 즉 Shared Lock끼리는 동시에 접근이 가능하다는 뜻, 여러 트랜잭션이 동시에 한 Row를 읽을 수 있다는 뜻
  - Shared Lock이 설정된 Row에 베타 락을 걸 수 없다.

## Exclusive Lock (X Lock)
- 쓰기 락으로 불리며 특정 Row를 변경하고자 할 때 사용된다.
- 베타 락이 걸린 row에 대해 다른 트랜잭션은 읽기, 쓰기 둘다 불가능하다.
- 특정 Row에 Exclusive Lock이 해제될 때까지, 다른 트랜잭션은 읽기 작업을 위해 Shared Lock을 걸거나, 쓰기 작업을 위해 Exclusive Lock을 걸 수 없다.
- 

<br>

InnoDB 에서는 대표적으로 3가지 Lock 범위가 존재한다.

## Record Lock
- 인덱스 레코드에 설정되는 lock, Row가 아니라 DB의 index record에 걸리는 Lock

  ```mysql
  SELECT * 
    FROM order 
   WHERE id = 1
     FOR UPDATE;
  ```

- id가 1에 해당하는 레코드에 lock이 걸리며 다른 트랜잭션에서 해당 레코드 변경 불가능



## Gap Lock
- Gap이란 실제 레코드가 없는 부분을 지칭한다.
- 인덱스 레코드 간 Gap || 인덱스 레코드 첫번째 || 마지막

  에 설정되는 Lock 으로써, 해당 범위에 접근하는 것을 제한

  ````mysql
  SELECT * 
    FROM order 
   WHERE id > 1
     FOR UPDATE;
  ````

- id가 1인 row만 있을 때, 
- id가 2인 데이터를 insert하려는 경우 불가능



## Next key Lock

- record Lock + 해당 인덱스 레코드 `앞`의 Gap Lock이 합쳐진것

```mysql
INSERT INTO order (id) values (1);
INSERT INTO order (id) values (3);
INSERT INTO order (id) values (5);

  SELECT * 
    FROM order 
   WHERE id >= 3
     FOR UPDATE;
```
- id가 1, 3, 5인 row가 존재할 때
- id >= 3을 만족하는 첫번쨰 인덱스 레코드 id = 3을 발견
- 첫번째 인덱스 발견 (3) 직전의 인덱스 레코드 1부터 3 사이에 gap lock 적용 (1 < gap lock < 3)
- id > 3 인 모든 인덱스 레코드들에 record lock 적용

<br>
<br>


<br>

# Consistent Non-Locking Reads
**기본적으로 SELECT 문을 통해 조회한 데이터는 Non-Locking Read 이다.**

그래서 특정 데이터가 `FOR UPDATE` 로 락이 걸린 상태라도 FOR UPDATE , FOR SHARE 가 없는 단순 SELECT 쿼리는

아무런 대기 없이 해당 데이터를 조회할 수 있다.

## MVCC
- 하나의 레코드에 대해 여러곳에서 여러 버전을 가지고 있다는 뜻으로
- InnoDB의 `Undo Log`를 이용하여 `스냅샷` 정보를 바탕으로 `Consistent Non Locking Read` 지원
    - **잠금없는** 일관된 읽기
    - 격리 수준에 따라 다름, 데이터 변경시 특정 시점에 조회될 수도 안될수도 있다.
    - 예로 READ COMMITTED은 동일 트랜잭션에서 SELECT마다 스냅샷 초기화
    - REPEATABLE READ는 동일 트랜잭션에서 최초 SELECT에만 스냅샷 초기화



<br>


# Isolation Level

## READ UNCOMMITED

- Non-Locking Read, MVCC 사용 X

- 다른 트랜잭션에 접근하여 커밋되지 않은 데이터를 읽을 수 있다.
- Dirty Read 발생
    - 커밋되지 않은 데이터를 읽음으로써 디비에 없는 데이터일 수 있기 떄문에 신뢰할 수 없는 읽기
- Non Repetable Read 발생
    - 한 트랜잭션에서 동일한 select 결과가 다름
- Phantom Read 발생
    - 이전 SELECT 결과에 없던 row가 생김



## READ COMMITED

- 다른 트랜잭션에서 커밋된 데이터만 읽을 수 있다.
- MVCC 사용하지만 동일한 트랜잭션 내에서 각 `SELECT` 문은 자체적으로 새로운 `Snapshot` 을 생성하기 때문에 아래 2가지 이슈가 발생
- Non Repetable Read 발생
    - 한 트랜잭션에서 동일한 select 결과가 다름
- Phantom Read 발생
    - 이전 SELECT 결과에 없던 row가 생김
- UPDATE, DELETE, Locking Read시
  record lock만 설정하고 gap lock을 사용하지 않는다.
  record lock을 설정하기 때문에 Dirty Read가 발생하지 않지만
  `Gap Lock` 을 설정하지 않기 때문에 범위 검색과 같은 쿼리에서 `Phantom Read` 현상 발생할 수 있음

- 인덱스 스캔 중 마주치는 모든 인덱스 레코드에 X-LOCK을 걸고 WHERE절이 수행될 때 조건에 부합하지 않는 레코드들에 대해 X-LOCK을 반환



## REPEATABLE READ

- Default level
- 최초 `SELECT` 문이 수행된 시점을 기준으로 `Snapshot` 이 생성되어
  다른 트랜잭션에 의해 해당 데이터가 변경되어도 `Undo Log` 에 저장된 내용을 기반으로 기존 데이터를 재구성
- 트랜잭션이 시작된 시점 이후로 여러 번 Select Row 를 확인해도 동일한 값을 갖는다
    - SELECT 시 현재 시점의 스냅샷을 만들어 진행
    - 동일 트랜잭션 내에 일관성 보장
- Dirty Read, Non Repetable Read 발생 X

- Phantom Read는 MVCC와 Locking read의 조합으로 발생할 수 있다.
- 일반적인 `SELECT` 문은 `Non-Locking Read` 를 기반으로 하기 때문에 `UPDATE` 를 위한 `SELECT` 는 `SELECT … FOR UPDATE` 와 같이 `Locking Read` 를 사용하는 것이 더 적합



- `Locking Read` `UPDATE` `DELETE` 문의 경우 record lock 과 gap lock이 발생
    - 고유 인덱스에 대한 쿼리는 `Record Lock`이 적용
    - 범위 검색 조건과 같은 다른 검색 조건의 경우 스캔한 인덱스 범위에 `Gap Lock` 또는 `Next-Key Lock` 을 설정
    - 인덱스 스캔 중 마주치는 모든 레코드에 대해 X-LOCK을 설정



### SERIALIZE
-  SELECT 문에 사용하는 모든 테이블에 shared lock이 발생
    - SELECT * FROM order FOR SHARE
-  S-Lock 이 설정된 다른 트랜잭션이 Row를 읽을 순 있지만 변경할 수 없다.