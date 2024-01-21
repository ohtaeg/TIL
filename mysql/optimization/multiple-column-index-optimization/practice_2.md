# Practice_2
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
### 2. 특정 고객의 특정 배송 상태 주문 조회
```sql
SELECT *
  FROM order
 WHERE customer_id = 3905
   AND status = '배송 완료' 
```
- 이런 경우 복합 인덱스를 설계할 때 어떤 컬럼을 선행 컬럼으로 해야할까?
- **고객 아이디와 주문 상태중 어떤 값이 카디널리티가 높을까?**
- `히스토그램`을 통해 카디널리티 분포를 확인해보자. 히스토그램에 대한 칼럼 통계 정보는 자동으로 수집되지 않기 때문에 수동으로 수집해야한다.
- `히스토그램`은 버킷(Bucket) 단위로 구분

```sql
ANALYZE TABLE orders UPDATE HISTOGRAM ON customer_id, status WITH 100 BUCKETS;
```

- information schema로 데이터베이스를 변경해서 히스토그램을 조회할 수 있다.

```sql
use information_schema;
    
SELECT *
  FROM COLUMN_STATISTICS
 WHERE TABLE_NAME = 'orders'
;
```

### 히스토그램 확인
- histogram/practice_2_customer_id_histogram.json
- 히스토그램에서 버킷 컬럼에 있는 데이터를 통해서 칼럼의 분포도를 확인할 수 있다.

> "histogram-type":"equi-height",

- 컬럼값의 범위를 균등한 개수로 구분하여 관리하는 히스토그램으로 범위별로 컬럼의 값 분포도가 정리되어 있다는 뜻.
- 각 버킷의 범위 시작 값과 마지막 값, 그리고 발생 빈도율과 각 버킷에 포함된 유니크한 값의 개수등 4개의 값을 가진다.

```
"buckets":[
    [
      1,
      110,
      0.01003584229390681,
      115
    ],
]
```
- 1 ~ 110 값의 비율은 0.01이라는 뜻 

<br>

- histogram/practice_2_status_histogram.json

> "histogram-type":"singleton",

- status에 대한 분포도 타입은 싱글톤 타입이다.
- 컬럼 값을 개별적으로 레코드 건수를 관리하는 히스토그램
- 싱글톤 히스토그램은 각 버킷이 컬럼의 값과 발생 빈도의 비율 총 2개 값을 가진다.

```json
{
  "buckets": [
    [
      "base64:type254:67Cw7IahIOyZhOujjA==",
      0.3324731182795699
    ],
    [
      "base64:type254:67Cw7IahIOykkQ==",
      0.6672939068100359
    ]
  ]
}
```