# EC2 배치그룹
- 인스턴스가 어떻게 배치되어야 하는지를 조정하기 원할 때 사용
- 워크로드에 따라 인스턴스 배치를 조정하는 것이 유리할 때가 있다.
    - 별도의 비용은 발생하지 않는다.

## 배치그룹 전략
1. `클러스터`

![cluster](./img/cluster.png)

- 인스턴스가 하나의 AZ에서 다함께 그룹화
    - 같은 하드웨어, 같은 AZ
- 통신 Latency가 낮은 장점
    - 인스턴스간 10gps
    -  빅데이터 업무시 사용
    - 저지연 고네트워크 어플리케이션에 사용
- 고성능 고위험
    - 같은 랙, 하드웨어에 있기에 하나가 실패하면 모든 인스턴스가 실패함 (실패전파)

    
2. `스프레드 (분산)`

![spread](./img/spread.png)

- AZ에 퍼져있는 배치 그룹을 다른 하드웨어로 분산, 즉 여러 AZ로 분산
- 배치 그룹당 최대 7개 인스턴스만 보유 가능하다.
- 실패를 최소화할 수 있다.
    - 최대 가용성을 가질 수 있다.

3. `파티션`

![partition](./img/partition.png)

- AZ안의 하드웨어의 여러 랙 세트에 분할
- AZ당 7개까지의 파티션을 가질 수 있다.
    - 서로 다른 파티션은 같은 하드웨어나 랙을 물리적으로 공유하지 않는다.
    - 실패를 최소화할 수 있다.
- 수백개의 EC2를 가질 수 있다.
- HDFS, HBase, 하둡, 카산드라, 카프카 등에 사용