# block - non-block
- I/O 종류
  - network (socket)
  - file
  - pipe (프로세스간 통신)
  - device (모니터, 키보드 etc)
  
## socket
- 네트워크 통신은 socket을 통해 데이터 입출력
- 백엔드 서버들은 네트워크 상의 요청자들과 각각 소켓을 열고 통신한다.


## block I/O
- I/O 작업을 요청한 프로세스/스레드는 요청이 완료될 때까지 블락됨
  
  ![img.png](img%2Fblock%2Fimg.png)

1. read blocking system call
2. read system call로 인해 kernel 모드로 전환 (context switching)
3. 해당 thread는 block이 되어 진행 X
4. device에 read i/o 요청 및 응답을 받음
5. 응답 데이터를 커널 space에서 유저 space으로 전송
6. blcok 되었던 스레드는 데이터를 읽고 실행

## socket block I/O
- 소켓이 Blocking 되는 경우는 다음과 같다.

    ![img.png](img/block/img_1.png)

- 소켓마다 두개의 버퍼를 가진다.
  - send buffer : 보낼 데이터를 저장
  - recv buffer : 받는 데이터를 저장 
- socket A, S가 있고 S가 A에게 데이터를 전송하려할 때 
1. 소켓 A에 read 시스템 콜을 호출한 스레드는 recv_buffer에 데이터가 들어올 때까지 block이된다.
2. 소켓 S에 write 시스템 콜을 호출한 스레드는 가끔 send_buffer가 가득차게 되면 여유 공간이 생길때 까지 block이 된다.

<br><br>

## non-blocking I/O
- 프로세스/스레드를 블락시키지 않고 요청에 대한 현재 상태를 즉시 리턴
- 블락되지 않고 즉시 리턴하기 때문에 스레드가 다른 작업을 수행할 수 있다.

  ![img.png](img/block/img_2.png)

1. read non-blocking system call
2. read system call로 인해 kernel 모드로 전환 (context switching)
3. device에 read i/o 요청 및 kernel -> thread로 즉시 리턴
4. thread 진행 (커널도 요청받은 read I/O를 계속 실행하여 응답 데이터를 준비)
5. 커널이 device로부터 응답을 받음
6. thread는 다시 read non-blocking system call
7. read system call로 인해 kernel 모드로 전환 (context switching)
8. 응답 데이터를 커널 space에서 유저 space으로 전송

## Socket non-block I/O
- 소켓이 non-blocking 되는 경우는 다음과 같다.

  ![img.png](img/block/img_3.png)

1. 소켓 A에 read 시스템 콜을 호출한 스레드는 recv_buffer에 데이터가 없다면 없다고 리턴하고 바로 종료
2. 소켓 S에 write 시스템 콜을 호출한 스레드는 send_buffer가 가득차있더라도 적절한 에러코드와 함께 리턴하고 바로 종료

### non-block I/O 이슈
- I/O 작업 완료를 어떻게 확인할 것인가?
1. 완료됐는지 주기적으로 반복적으로 확인
   - 완료된 시간과 완료를 확인한 시간과의 차이가 존재할 수 있어 응답 속도가 느려질 수 있음
   - 반복적인 확인 작업은 CPU 낭비
2. I/O multiplexing (다중 입출력) 사용