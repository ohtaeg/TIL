# block - non-block
- I/O 종류
  - network (socket)
  - file
  - pipe (프로세스간 통신)
  - device (모니터, 키보드 etc)
  
## socket
- 네트워크 통신은 socket을 통해 데이터 입출력
- 백엔드 서버들은 네트워크 상의 요청자들과 각각 소켓을 열고 통신한다.
- 특정 포트를 통해 양방향 통신이 가능하도록 만들어주는 추상화된 장치


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
  - send buffer : 보낼 데이터를 저장하는 송신 버퍼
  - recv buffer : 받는 데이터를 저장하는 수신 버퍼
- socket A, S가 있고 S가 A에게 데이터를 전송하려할 때 
1. 소켓 A에 read 시스템 콜을 호출한 스레드는 recv_buffer에 데이터가 들어올 때까지 block이 된다.
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

- non-block I/O 이슈, I/O 작업 완료를 어떻게 확인할 것인가?
1. 완료됐는지 주기적으로 반복적으로 확인
   - 완료된 시간과 완료를 확인한 시간과의 차이가 존재할 수 있어 응답 속도가 느려질 수 있음
   - 반복적인 확인 작업은 CPU 낭비
2. `I/O multiplexing` (다중 입출력) 사용
   - I/O 작업들을 동시에 모니터링하고 그 중 완료된 I/O 작업들을 한번에 알려준다.
3. callback / signal 사용
   - POSIX AIO : Portable Operating System Interface, 이식이 가능한 OS 인터페이스
     - 여러 OS에서 사용할 수 있도록 표준을 정하여 공통 API를 정리한 스펙 
   - LINUX AIO : 리눅스 커널 자체

<br><br>

## I/O multiplexing
- 각 클라이언트 마다 별도의 스레드를 생성하는 것이 아닌 하나의 스레드에서 다수의 클라이언트에 연결된 소켓하나라도 들어온 소켓에 OS 혹은 커널에서 알려주는 방식
- 소켓에 이벤트(read/write)가 발생할 경우에만 별도의 스레드를 만들어 (혹은 스레드 풀의 스레드 사용) 해당 이벤트를 처리하도록 구현
    
  ![img.png](img/block/img_4.png)

1. thread가 커널에게 I/O multiplexing 기반 non-blocking read system call 요청
   - monitor 2 sockets non-blocking read : 2개의 소켓에 대해서 non-blocking read 요청, 새로운 데이터가 들어오면 알려달라.
2. read system call로 인해 kernel 모드로 전환 (context switching)
3. device에 read i/o 요청
4. sytem call을 호출했던 thread는 block 되거나 run이 된다. (현재는 block 이라고 가정)
5. 커널이 device로부터 응답을 받음
6. 커널에서 스레드에게 데이터가 들어왔다고 알려준다.
7. 스레드는 첫번째 소켓으로부터 데이터를 읽어온다.
8. 스레든는 두번째 소켓으로부터 데이터를 읽어온다.

### I/O multiplexing 종류
- 커널에서는 아래와 같이 하나의 스레드가 여러 개의 소켓을 관리할 수 있는 시스템 콜을 제공한다. 
  - select
  - poll
  - epoll
  - kqueue
  - IOCP (I/O completion port)

<br>

- select 와 poll 은 성능면에서 좋지않아 잘 활용하지 않음, epoll, kqueue, IOCP가 더 많이 활용된다.
- epoll은 리눅스 계열에서 많이 사용
- kqueue은 맥 OS에서 많이 사용
- IOCP는 윈도우나 솔라리스 계열에서 사용


