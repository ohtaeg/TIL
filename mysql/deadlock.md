# 데드락 로그 분석
- 회사에서 간헐적으로 데드락이 발생하여 로그 분석을 해보았다.

```sql
MySQL> show engine innodb status;
```

- 여러 정보에 대한 로그를 확인할 수 있는데 데드락은 **LATEST DETECTED DEADLOCK** 부분을 확인하면 된다.
- 모든 로그를 해석할 수가 없어 해석되는 일부분만 가져와보았다.

```text
2023-01-27 03:36:42 0x1510163ff700
## deadlock 발생한 시간

*** (1) TRANSACTION:
## 첫번째 트랜잭션, insert 쿼리가 실행되고 있음

TRANSACTION 21124363, ACTIVE 0 sec inserting
## TRANSACTION ID는 21124363이며, 한개의 ACTIVE 세션이 시작하면서 insert ddl 실행

mysql tables in use 1, locked 1
## mysql 테이블들중에 1개를 사용중이고 1개의 lock이 존재 

LOCK WAIT 6 lock struct(s), heap size 1136, 3 row lock(s), undo log entries 2
## 이 부분은 좀 더 해석이 필요해보인다. 1136바이트 만큼 힙사이즈를 차지 

MySQL thread id 172100, OS thread handle 23159068354304, query id 36193708 host admin update
## 호스트 172100 쓰레드에서 사용중인 계정은 admin이며 update중이 였는데 update 내역은 다음과 같다.
insert into A 어쩌구 저쩌구
## A 테이블에 대해서 insert 쿼리를 실행

*** (1) HOLDS THE LOCK(S):
## 첫번째 트랜잭션에서 잡고있는 lock 정보

RECORD LOCKS space id 380 page no 22 n bits 136 index PRIMARY of table `B`.`B` trx id 21124363 lock_mode X 
locks rec but not gap
## B 테이블의 기본키에서 발생하며, RECORD LOCK을 lock_mode X인 Exclusive Lock 사용

*** (1) WAITING FOR THIS LOCK TO BE GRANTED:
## 1번 트랜잭션이 사용하기 위해 기다리고 있는 LOW에 대한 정보

RECORD LOCKS space id 334 page no 8 n bits 192 index PRIMARY of table `A`.`A` trx id 21124363 lock_mode X 
insert intention waiting
## AA 테이블의 primary index 영역의 page no 8 n bit 192 부분을 lock 걸려고 기다리고 있으며
```

- 위 1번 트랜잭션에서 A 테이블에 대해 insert를 수행하고 있으며
- insert를 수행하기 위해 다른 트랜잭션으로부터 A 테이블에 대한 LOCK을 얻기 위해 기다리고 있다.
  - 이때 A 테이블의 page no 8 n bit 192 부분에 대해 RECORD LOCK을 걸려고 하고 있다.
- 1번 트랜잭션이 가지고 있는 LOCK은 B 테이블의 기본키에서 발생한 page no 22 n bits 136 부분의 RECORD LOCK이며 Exclusive Lock이다.
  - PK에 대한 인덱스를 탔기 때문에 Table-Level Lock은 걸리지 않는다.

<br>
<br>

```
*** (2) TRANSACTION:
## 두번째 트랜잭션, update 쿼리가 실행되고 있음

TRANSACTION 21124362, ACTIVE 1 sec starting index read
## TRANSACTION ID는 21124362이며, 한개의 ACTIVE 세션이 시작하면서 인덱스를 읽고 있음.

mysql tables in use 1, locked 1
LOCK WAIT 8 lock struct(s), heap size 1136, 399 row lock(s), undo log entries 127
## 이 부분은 좀 더 해석이 필요해보인다.

MySQL thread id 172078, OS thread handle 23159068083968, query id 36193760 host admin updating
## 호스트 172078 쓰레드에서 사용중인 계정은 admin이며 update중이 였는데 update 내역은 다음과 같다.
update B 어쩌구 저쩌구
## B 테이블에 대해서 update 쿼리를 실행


*** (2) HOLDS THE LOCK(S):
## 2번 트랜잭션이 잡고있는 LOCK 정보

RECORD LOCKS space id 334 page no 8 n bits 192 index PRIMARY of table `A`.`A` trx id 21124362 lock_mode X
## A 테이블의 기본키에서 발생하며, RECORD LOCK을 lock_mode X인 Exclusive Lock 사용
## A 테이블의 page no 8 n bits 192 부분에 대해 Lock을 걸고 있음

*** (2) WAITING FOR THIS LOCK TO BE GRANTED:
## 2번 트랜잭션이 사용하기 위해 기다리고 있는 LOW에 대한 정보

RECORD LOCKS space id 380 page no 22 n bits 136 index PRIMARY of table `B`.`B` trx id 21124362 lock_mode X 
locks rec but not gap waiting
## B 테이블의 primary index 영역의 page no 22 n bit 136 부분을 lock 걸려고 기다리고 있다.
```

- 위 2번 트랜잭션에서 B 테이블에 대해 update 쿼리를 수행하고 있으며
- update를 수행하기 위해 다른 트랜잭션으로부터 B 테이블의 Lock을 얻기 위해 기다리고 있다.
  - 이때 B 테이블의 page no 22 n bit 136 부분에 대해 RECORD LOCK을 걸려고 하고 있다.
- 2번 트랜잭션이 가지고 있는 LOCK은 A 테이블의 기본키에서 발생한 page no 8 n bits 192 부분의 RECORD LOCK이며 Exclusive Lock이다.
    - PK에 대한 인덱스를 탔기 때문에 Table-Level Lock은 걸리지 않는다.

<br>

### 로그 내용 정리
- 1번 트랜잭션은 A 테이블에 대해 insert를 하려고 하며 B 테이블의 특정 row에 lock을 잡고 있다.
- 2번 트랜잭션은 B 테이블에 대해 update를 하려고 하며 A 테이블의 특정 row에 lock을 잡고 있다.
- 서로 lock을 잡고 얻기위해 대기하다보니 데드락이 발생하였다.
- 근본적인 발생 원인은 알 수 없으나 우선적으로 외래키에 의한 잠금 전파일 가능성을 고려해본다.