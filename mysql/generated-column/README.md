# Generated Column
- [mysql docs](https://dev.mysql.com/doc/refman/8.4/en/create-table-generated-columns.html)
- 5.7 버전에서 도입된 가상 컬럼 기능
- 특정 `컬럼`에 계산식이나 표현식을 지정하여 지정된 식을 기반으로 <br>
  계산되어 생성되거나 지정된 표현 형태로 출력을 하는 기능

## Generated Column을 도입하게 된 이유
- 레거시 주문 시스템(v1)과 신규 주문시스템(v2)은 주문 테이블에 구매자 전화번호를 저장하고 있음 (ex 01012345678)
- v1과 v2둘다 전화번호 컬럼에 인덱스가 걸려있는 상태
- 문제는 화면에서 주문목록에서 전화번호로 주문을 조회할 때 v1에서 전화번호 검색을 full로 하지만 v2에서는 뒷자리 번호로만 하다보니 v2에서는 인덱스가 걸리지 않는 상황
- Generated Column을 도입하여 인덱스를 타도록 도입

<br>

## 어떻게 해결하였는가?
Generated Columns 을 사용하여 전화번호 뒷자리를 저장하는 가상 컬럼을 추가한 후 `STORED` 타입으로 지정

`STORED` 타입으로 지정한 이유는 다음과 같다.
1. `VIRTUAL` 타입은 데이터를 저장하지 않기에 메모리를 차지하지 않지만 요청할때마다 계산되어진다.
2. `STORED` 타입은 함수나 수식에 의해서 평가 및 계산된 값이 저장되는 방식이기 때문에 실제로 저장 공간을 차지.
값이 변경될떄마다 새로운 값을 계산하고 해당 값을 업데이트한다.

`STORED`으로 한 궁극적인 이유는 주문의 전화번호는 스냅샷 개념으로 한번 저장되면 변경될 일이 거의 없다.

그러다보니 실제 값을 저장하는 단점이 있지만 사전에 계산된 값을 저장하기 때문에 다시 계산하지 않아도 빠르게 조회되는 장점으로 트레이드 오프함

<br>
<br>

- 데이터를 준비하고 인덱스를 걸어 확인해보자.

```mysql
create table `order`
(
    id        int auto_increment,
    phone     varchar(20) not null,
    constraint order_pk primary key (id)
);

alter table `order`
  add gen_phone varchar(20) as (substr(`phone`, -(4))) stored;

create index idx_phone
  on `order` (phone);

create index idx_gen_phone
  on `order` (gen_phone);

insert into `order` (phone) VALUES (01012345678);
insert into `order` (phone) VALUES (01011111111);
```
위와 같이 각 컬럼에 인덱스를 세팅하고 데이터를 넣고 우선 phone 컬럼에 뒷자리 번호를 검색하는 경우 
'%;인덱스를 타지 않음을 확인할 수 있다.

```mysql
EXPLAIN
 SELECT *
   FROM `order`
  WHERE phone like '%5678';
```

| id | select_type | table  | partitions | type | possible_keys | key | key_len | ref | rows | filtered | extra       |
|----|-------------|--------|------------|------|---------------|-----|---------|-----|------|----------|-------------|
| 1  | SIMPLE      | order  |            | ALL  |               |     |         |     | 2    | 50       | Using where |
- `type` 컬럼은 테이블의 레코드를 어떤 방식으로 읽었는지를 나타내는데, 인덱스를 사용했는지 테이블을 처음부터 끝까지 읽었는지 등을 의미한다.
- 여기서 `ALL` 이란 `테이블 풀 스캔`을 의미하며 가장 비효율적인 방법을 뜻한다.
  - 아직 인덱스가 없어서 테이블 풀 스캔을 하고있다.

<br>

- 가상 컬럼에 실행 계획을 확인해보자.
```mysql
EXPLAIN
 SELECT *
   FROM `order`
  WHERE gen_phone = '5678';
```
| id | select_type | table | partitions | type | possible_keys | key           | key_len | ref   | rows | filtered | extra |
|----|-------------|-------|------------|------|---------------|---------------|---------|-------|------|----------|-------|
| 1  | SIMPLE      | order |            | ref  | idx_gen_phone | idx_gen_phone | 83      | const | 1    | 100      |       |

- 다음과 같이 인덱스가 걸림을 확인할 수 있다. 