# EBS

- Elastic Block Store
- 인스턴스가 작동하는 동안 연결할 수 있는 네트워크 드라이브
    - 인스턴스가 종료된 이후에도 데이터 유지
- 보통 하나의 EC2 인스턴스에 한 개의 EBS가 설치될 수 있다.
    - 다만 인스턴스 타입 (io1, io2 family)에 한정적으로 동일한 EBS 볼륨을 동일한 AZ의 여러 EC2 인스턴스에 연결이 가능하다.
- EBS 볼륨 생성시 `특정 AZ에 바인딩된다.`
    - a에서 생성된다면 b에 붙을 수 없다.
    - 스냅샷을 통해 다른 AZ로 옮길 수 있다.
- EC2는 두가지 종류의 스토리지가 존재한다.

1. EBS Based : 반 영구적인 파일의 저장
    - 스냅샷 기능
    - Stop이 가능함. (중지가 가능하다.)
    - EBS 타입의 EC2는 실제 하드 디스크랑 인스턴스랑 분리되어 있고 네트워크로 연결되어 잇다.
    - EC2와 분리되어 있기 때문에 EC2 인스턴스가 내려가도 EBS는 살아있어서 또 사용가능
    - 즉, 컴퓨팅 파워와 파일이 종속적이지 않음
    - ![ebs](./img/ebs.png)
2. Instance Storage : 휘발성, 빠른 방식
    - 빠르지만 저장이 필요 없는 경우
    - Stop이 불가능함
    - 인스턴스 스토리지는 EC2 안에 물리적으로 연결되어 있다.
    - 그래서 바로 접근이 가능해서 빠르다.
    - EC2를 내려버리면 인스턴스 스토리지도 날라간다.
    - 그렇기에 다시 올리면 기존 파일을 저장할 수 없다.
    - 빠른 연산이 필요하고 굳이 파일 저장할 필요가 없을 때
    - ![instance-storage](./img/instance-storage.png)
 
<br>

# 인스턴스 스토리지

- EBS 볼륨은 성능은 좋지만 제한된 네트워크 드라이브
    - 만약 더 높은 I/O 퍼포먼스를 원한다면 EC2 인스턴스 스토어를 사용하면 된다.



<br>



### Delete on Termination attribute

- EBS 볼륨을 생성할 때 Delete on Termination 옵션이 있다.
- EC2 인스턴스가 종료될 때 함께 삭제할지 말지 여부



### 루트 볼륨

- 인스턴스를 시작하면 인스턴스 부팅에 사용된 볼륨
- 기본적으로 루트 볼륨 유형은 인스턴스 종료 시 삭제 속성이 기본적으로 선택되어 있으므로 같이 삭제된다.

- Amazon EC2 `인스턴스 스토어`가 지원하는 AMI Amazon `EBS`에서 지원하는 AMI 중에서 선택 가능



## 스냅샷

- 원하는 어떤 시점에 데이터 백업 가능
- 특정 AZ에 종속되지 않는다.
    - A라는 AZ의 EBS를 B AZ에 옮기고 싶을 때 스냅샷을 이용하여 옮길 수 있다.



<br>



# AMI

- Amazon Machine Image
- 동일한 구성의 인스턴스가 여러개 필요한 경우 한 AMI에서 여러 인스턴스를 시작할 수 있다.
- AMI에는 OS만 설치되는 형식이 아니고 OS를 설치해두고 각종 서버 어플리케이션, 데이터베이스 등 모든 설치된 프로그램까지 백업이 된다.
- 특정 region용으로 각 리전마다 고유하게 구축된다.
    - 다른 AWS 리전의 AMI를 사용하여 EC2 인스턴스를 런칭할 수는 없지만 AMI를 대상 AWS 리전에 복사한 다음 이를 사용하여 EC2 인스턴스를 생성 가능
- AWS에서 제공하는 Public AMI와 사용자가 만들 수 있는 AMI가 있다.



<br>



## EBS Volume Types

1. gp2 / gp3 (SSD)
    - `범용 목적 SSD`
    - Low Latency, 비용 효율적인 스토리지
    - Size : 1GB ~ 16TB
    - gp2
        - 할당된(프로비저닝된) 용량 크기에 따라 성능이 정해진다.
        - gp2는 `GB당 3 IOPS`로 `1TB = 3,000 IOPS` 를 제공
            - 16,000 IOPS를 얻으려면 5,333GB 볼륨의 크기가 필요
        - 용량 크기에 비해 IOPS가 많으면 3,000 IOPS까지 burst
    - gp3
        - `3,000` IOPS ~ `16,000`
        - 초당 처리량 125메가바이트 ~ 1000메가바이트

2. io1 / io2 (SSD)
    - `프로비저닝된 IOPS SSD`
    - 고성능 SSD
    - `16,000` IOPS ~ `64,000` IOPS (`Nitro 인스턴스에서 64,000`)
        - 만약 Nitro 인스턴스가 아니라면 최대 `32,000` IOPS
    - 보통 데이터베이스 워크로드가 있는 곳에서 사용
    - Size : 4GB ~ 16TB
    - 같은 값일 때, io1에 비해 io2가 기가바이트당 IOPS가 더 많아서 io2를 사용하는게 일반적
    - Supprot EBS Multi attach - io1/io2 family
        - 같은 AZ 내에 여러 EC2에 같은 EBS 볼륨을 attach 가능
    - io2 Block Express
        - 4GB ~ 64 TB
        - MAX 256,000 IOPS
            - 기가바이트당 IOPS 비율이 1,000 : 1

4. St1 (HDD)

    - `처리량 최적화 HDD`
        - 빅데이터, 로그 프로세싱, 데이터 웨어하우스에 탁월
    - Max `500 IOPS`
    - MAX 500MB throughput

    - 저비용, 자주 엑세스하는 처리량 워크로드 용
    - 부팅 볼륨이 될 수 없다.
    - 125MB ~ 16TB

5. sc1 (HDD)
    - `Cold HDD`
    - `가장 저렴한 비용이 필요할 때` , 자주 엑세스하지 않을 때
    - MAX 250 IOPS
    - MAX 250MB throughput
    - 부팅 볼륨이 될 수 없다.
    - 125MB ~ 16TB



<br>



# EBS Encryption

- 암호화된 EBS 볼륨을 생성할 때

    - 저장 데이터는 볼륨 내에서 암호화가 된다.
    - 인스턴스 <-> 볼륨 사이에 이동중인 모든 데이터가 암호화된다.
    - 스냅샷도 암호화 된다.

- 암호화는 Latency에 매우 적은 영향을 끼치기에 거의 없다.

- `AWS KMS`의 키를 활용 (AWS Key Management Service)

    - `AES-256`
    - Advanced Encryption Standard
    - 키가 256bit(=32byte)

- 암호화되지 않은 EBS 볼륨(우리가 생성한 볼륨)을 어떻게 암호화 하는가?

    - 두가지 방법이 있다.

    1. EBS 볼륨에서 `스냅샷`을 생성한 후 스냅샷 `복사 기능`을 사용하여 암호화

        - 복사할 때 옵션으로 암호화를 할 수 있다.

        - 복사하여 암호화된 스냅샷으로 EBS 볼륨을 생성하게 되면 볼륨이 암호화가 된다.

    2. 암호화되지 않은 스냅샷에서 바로 볼륨 생성할 때 옵션으로 암호화할수도 있다.