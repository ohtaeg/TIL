# Pratice
- 기존 인덱스를 커버링 인덱스로 변경해서 성능 향상 해보기
- 온라인 쇼핑몰의 주문 관리 시스템

<br>


### 고객의 최근 주문 조회 쿼리
- 현재 아래와 같은 인덱스를 설정하고 실행 계획을 보면 다음과 같다.
```sql
EXPLAIN
SELECT customer_id, order_date, status
  FROM orders
 WHERE customer_id = 10000
 ORDER BY order_date DESC
 LIMIT 10;
```
```sql
CREATE INDEX idx_customer_id_order_date ON orders(customer_id, order_date);
```

| id | select_type | table  | partitions | type | possible_keys              | key                        | key_len | ref   | rows | filtered | extra                |
|----|-------------|--------|------------|------|----------------------------|----------------------------|---------|-------|------|----------|----------------------|
| 1  | SIMPLE      | orders |            | ref  | idx_customer_id_order_date | idx_customer_id_order_date | 5       | const | 104  | 100      | Backward index scan  |

- type 컬럼이 `ref`로 되어있는데 이는 인덱스를 동등 조건으로만 이용해서 검색한 경우를 뜻한다.
  - **효율적인 접근이라는 뜻**
- MySQL 엔진이 스토리지 엔진으로부터 가져온 데이터 갯수는 104개이며 필터링이 100임으로 필터링도 잘 되었다.


<br>

```sql
EXPLAIN ANALYZE
SELECT customer_id, order_date, status
  FROM orders
 WHERE customer_id = 10000
 ORDER BY order_date DESC
 LIMIT 10;
```
```
-> Limit: 10 row(s)  (cost=36.4 rows=10) (actual time=0.453..0.456 rows=10 loops=1)
    -> Index lookup on orders using idx_customer_id_order_date (customer_id=10000) (reverse)  (cost=36.4 rows=104) (actual time=0.452..0.455 rows=10 loops=1)
```
- 실행 쿼리 시간도 대략 0.4초가 걸렸다.

- 여기서 커버링 인덱스를 적용해보자.

<br>
<br>
<br>

```sql
CREATE INDEX idx_covering ON test.orders(customer_id, order_date, status);

EXPLAIN
SELECT customer_id, order_date, status
FROM test.orders
WHERE customer_id = 10000
ORDER BY order_date DESC
    LIMIT 10
;

```


| id | select_type | table  | partitions | type | possible_keys                            | key           | key_len | ref   | rows | filtered | extra                             |
|----|-------------|--------|------------|------|------------------------------------------|---------------|---------|-------|------|----------|-----------------------------------|
| 1  | SIMPLE      | orders |            | ref  | idx_customer_id_order_date,idx_covering  | idx_covering  | 5       | const | 104  | 100      | Backward index scan; Using index  |

- 위 실행계획과 커버링 인덱스의 실행계획 차이를 살펴보자
- 커버링 인덱스가 적용되었는지 확인하려면 extra 컬럼에 `Using index` 라는 메시지가 있는지이다.
- 실행 속도도 비교해보면 확연히 차이가 난다. (0.4 -> 0.04)

```
-> Limit: 10 row(s)  (cost=14.1 rows=10) (actual time=0.0463..0.0492 rows=10 loops=1)
    -> Covering index lookup on orders using idx_covering (customer_id=10000) (reverse)  (cost=14.1 rows=104) (actual time=0.0453..0.0477 rows=10 loops=1)
```

- 이는 SELECT 문에 필요한 모든 컬럼을 인덱스가 다 가지고 있어서 인덱스 스캔만으로 결과를 낼 수 있어 커버링 인덱스가 적용이 되었다.