# Practicce
- 복합 인덱스 설계 및 잘못 설계된 경우와 성능 차이 비교
- 온라인 쇼핑몰의 주문 관리 시스템

```sql
CREATE TABLE `orders` (
    `order_id` INT  NOT NULL AUTO_INCREMENT,
    `customer_id` INT,
    `order_date` DATE,
    `product_id` INT,
    `quantity`   INT,
    `status`     VARCHAR(50),
    PRIMARY KEY (`order_id`)
) ENGINE=InnoDB;
;
```

## 어플리케이션에서 자주 발생하는 유형
### 1. 특정 고객의 특정 날부터 최근 주문 조회
```sql
SELECT *
  FROM order
 WHERE customer_id = 3905
   AND order_date > '2023-11-04'
 ORDER BY order_date DESC 
 LIMIT 10; 
```
- 이런 경우 복합 인덱스를 설계할 때 어떤 컬럼을 선행 컬럼으로 해야할까?
- 일반적으로 **주문 날짜보다 고객 id가 카디널리티가 높을 것이다.**
- 게다가 현재 주문 날짜는 범위 기반의 조건으로 사용되기 때문에 범위 인덱스 탐색이 이루어질 것이기 때문에 <br>
주문 날짜를 선행 컬럼으로 사용할 경우 쿼리가 비효율적인 탐색할 확률이 높다.
- 인덱스 없는 현재 실행 계획을 보면 다음과 같다.

| id | select_type | table  | partitions | type | possible_keys | key | key_len | ref | rows    | filtered | extra                       |
|----|-------------|--------|------------|------|---------------|-----|---------|-----|---------|----------|-----------------------------|
| 1  | SIMPLE      | orders |            | ALL  |               |     |         |     | 996334  | 3.33     | Using where; Using filesort |

- `type` 컬럼은 테이블의 레코드를 어떤 방식으로 읽었는지를 나타내는데, 인덱스를 사용했는지 테이블을 처음부터 끝까지 읽었는지 등을 의미한다.
- 여기서 `ALL` 이란 `테이블 풀 스캔`을 의미하며 가장 비효율적인 방법을 뜻한다.
  - 아직 인덱스가 없어서 테이블 풀 스캔을 하고있다. 

<br>

#### 범위 조건인 주문 날짜를 선행컬럼으로 인덱스 생성
- 먼저 주문 날짜를 선행 컬럼으로 복합 인덱스를 만들고 실행계획을 보면
```sql
CREATE INDEX idx_order_date_customer_id ON orders(order_date, customer_id);
```
| id | select_type | table  | partitions | type  | possible_keys              | key                        | key_len | ref | rows   | filtered | extra                                      |
|----|-------------|--------|------------|-------|----------------------------|----------------------------|---------|-----|--------|----------|--------------------------------------------|
| 1  | SIMPLE      | orders |            | range | idx_order_date_customer_id | idx_order_date_customer_id | 3       |     | 319118 | 10       | Using index condition; Backward index scan |

- 타입 컬럼이 ALL -> `range` 로 변경됨을 확인할 수 있다. 즉 `인덱스 레인지 스캔`을 하고 있다.
- `rows` 컬럼이 90만에서 31만으로 줄어들었고 filtered 컬럼은 3.33 -> 10으로 증가했다.
  - 즉, `rows` 컬럼은 **SQL 문을 통해 MySQL 엔진이 스토리지 엔진으로부터 가져온 데이터 갯수**를 뜻하며 90만개에서 31만개로 줄어들었고  
  - `filtered` 컬럼이 10이란 뜻은 가져온 데이터를 필터 조건에 따라 제거된 비율을 뜻한다.<br>
  즉, 31만 개중 10%만 유효하다는 것을 뜻한다. 필터링되고 남은 레코드는 3만 1천건(31만건 * 10%)이라는 의미이다. <br>
    통계 정보로부터 예측된 값이기 때문에 실제 값이 아님을 참고하자.
- range 접근 방법도 상당히 빠르지만 filtered를 통해 아직도 비효율적인 탐색을 하고있음을 알 수 있다. 

<br>

- `ANALYZE`를 이용하여 쿼리가 실제로 어떻게 처리되는지 그리고 소요시간이 얼마인지 확인해보자.
```sql
EXPLAIN ANALYZE SELECT *
  FROM orders
 WHERE customer_id = 3905
   AND order_date > '2023-11-04'
  ORDER BY order_date desc
  LIMIT 10
;

-> Limit: 10 row(s)  (cost=100427 rows=10) (actual time=88.7..88.7 rows=6 loops=1)
    -> Index range scan on orders using idx_order_date_customer_id over ('2023-11-04' < order_date) (reverse), with index condition: ((orders.customer_id = 3905) and (orders.order_date > DATE'2023-11-04'))  (cost=100427 rows=319118) (actual time=88.7..88.7 rows=6 loops=1)
```
- 88ms 정도 걸린걸 확인할 수 있다.

<br>

#### 카디널리티가 높은 고객 id를 선행컬럼으로 인덱스 생성
- 이번엔 고객 id를 선행 컬럼으로 인덱스를 다시 생성하고 실행계획을 살펴보자.
```sql
CREATE INDEX idx_customer_id_order_date ON orders(customer_id, order_date);
```
| id | select_type | table  | partitions | type  | possible_keys                                         | key                        | key_len | ref | rows | filtered | extra                                      |
|----|-------------|--------|------------|-------|-------------------------------------------------------|----------------------------|---------|-----|------|----------|--------------------------------------------|
| 1  | SIMPLE      | orders |            | range | idx_order_date_customer_id,idx_customer_id_order_date | idx_customer_id_order_date | 9       |     | 6    | 100      | Using index condition; Backward index scan |

- type이 변함없이 인덱스 레인지 스캔을 하고 있지만, row는 6개로 줄어들었고 filtered가 100이다.
- 옵티마이저가 판단하여 읽어야하는 갯수가 31민개에서 6개로 줄어들었으며
- 그중 필터링 되고 남은 갯수가 6개(6 * 100%) 라는 뜻으로 읽은 데이터 갯수와 거르는 데이터 갯수가 얼마 없어 효율적인 탐색임을 알 수 있다.

<br>

- 쿼리의 소요시간도 확인해보자.
```sql
EXPLAIN ANALYZE SELECT *
  FROM orders
 WHERE customer_id = 3905
   AND order_date > '2023-11-04'
  ORDER BY order_date desc
  LIMIT 10
;

-> Limit: 10 row(s)  (cost=2.96 rows=6) (actual time=3.1..3.11 rows=6 loops=1)
    -> Index range scan on orders using idx_customer_id_order_date over (customer_id = 3905 AND '2023-11-04' < order_date) (reverse), with index condition: ((orders.customer_id = 3905) and (orders.order_date > DATE'2023-11-04'))  (cost=2.96 rows=6) (actual time=2.3..2.31 rows=6 loops=1)
```
- 88.7ms -> 3.1ms로 대폭 줄었다.
- 정리하자면 주문 날짜는 **범위 기반으로 탐색되기 때문에 선행 컬럼으로는 적합하지 않다.**

