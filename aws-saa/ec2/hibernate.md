# EC2 Hibernate
- 수면(Hibernation) , 최대 절전 모드
- 인스턴스를 최대 절전 모드 상태로 설정하면 RAM의 모든 상태가 루트 EBS 볼륨에 있는 파일에 저장된 다음 인스턴스가 종료된다.
    - 파일로 덤프되기 때문에 루트 EBS 볼륨은 암호화되어야 한다.
    - EC2 인스턴스 루트 볼륨 유형이 EBS 볼륨이어야 하며 인스턴스 스토어 볼륨은 안된다.
- 인스턴스 부팅시 OS는 중단된 상태가 아니기에 빠르게 재시작 된다.
- 사용 사례
    - 장기 실행 프로세스를 계속 실행하려는 경우
    - RAM 상태를 저장하는 경우
    - 초기하하는데 많은 시간이 걸리는 서비스가 있을 경우
    
<br>

### Hibernate 특징
- 모든 인스턴스에 적용이 되지 않는다.
    - C3, C4, C5
    - M3, M4, M5
    - R3, R4, R5
- 인스턴스의 RAM 사이즈는 `반드시 150GB보다 작아야한다.`
- 지원하는 AMI
    - Amazon Linux 2
    - Linux 1 AMI
    - Ubuntu
    - Windows
- 지원하는 인스턴스 타입
    - 온디맨드
    - 예약 인스턴스
    - **스팟 인스턴스는 지원 X**
- 60일이상 최대절전모드를 할 수 없다.