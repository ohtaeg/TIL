## Life Cycle

![lifecycle](./img/lifecycle.png)


### Pull
- Registry(docker hub)에서 Image `Pull`
- 도커 이미지 저장소에서 Image를 다운

### Push
- Image `Push` Registry(docker hub)
- 도커 이미지 저장소에 Image 업로드
  - `Push` 할 때 **권한이 필요함**

### Create
- Image `Pull` 후 이미지 자체는 **실행이 불가능한 상태**
- `Pull`된 Image를 실행하려면 `Container`로 만들어주는 작업이 필요하다.
  - 이때 작업은 `CREATE` 라는 명령어를 실행하면 **Image가 Container가 된다.**

### Start
- `Container` 를 실행시킨다 => 메모리에 적재하여 컨테이너의 어플리케이션이 동작하게 한다는 뜻
- `Start` 명령어를 실행한다.

### RUN
- `PULL` + `CREATE` + `START`
- 이 3가지 명령어가 한번에 실행이 된다.
- 만약 이미 Pulling된 이미지를 Run 명령어를 실행하면 다시 받지 않고 `CREATE` + `START` 만 실행
- 만약 `Run`을 수행해서 `Container`를 만든 상태에서 또 `Run`을 수행한다면 또 `Container`가 만들어진다.
  - 불필요한 컨테이너가 계속 만들어지게 된다.
- **`Run` 명령어 같은 경우는 `CREATE`가 필요한 경우에만 사용하도록 제한하는게 좋다.**
  - `CREATE` 와 `START`를 따로 사용하도록 하자.

### STOP
- 메모리에 적재된 `Container`를 중지하는 방법

### RM
- ReMove
- 중지된 `Container`를 삭제한다.

### RMI
- ReMove Image
- 이미지를 지운다.

### COMMIT
- `Container`를 다시 Image로 만드는 작업
- 사용하고 있던 기존 컨테이너를 이미지화 할 수 있다.
- 어플리케이션을 세팅하고 필요한 파일을 다운받고 이미지화 할 수 있다.

<br>
<br>

# 이미지

## 도커 이미지 다운로드
```shell
$ sudo docker pull nginx
```

## 도커 이미지 목록
```shell
$ docker images
```

## 도커 이미지 삭제 도움말
```shell
$ docker rmi -h
```

## 도커 이미지 삭제
```shell
$ sudo docker rmi nginx
```
- 삭제시 `image is referenced in multiple repositories` 에러가 발생할 경우
- `docker rmi {repository}/{imageName}:{tag}
```
REPOSITORY                   TAG       IMAGE ID       CREATED         SIZE
confluentinc/ksqldb-server   0.6.0     fd7093cdc5f1   2 years ago     844MB
confluentinc/ksqldb-server   latest    fd7093cdc5f1   2 years ago     844MB
```
```shell
$ sudo docker rmi confluentinc/ksqldb-server:0.6.0
```

## 도커 이미지 삭제
```shell
$ sudo docker rmi -f nginx
```
- 컨테이너는 실행중인데 이미지만 지우고 싶을 때



<br>
<br>
<br>

# 컨테이너

## 컨테이너 생성
```shell
$ sudo docker run -d --name tc tomcat
```
- `-d`: 백그라운드 실행
- `--name {컨테이너 이름}` : 컨테이너에 이름 부여

## 실행중인 컨테이너 확인
```shell
$ sudo docker ps
```


## 모든 컨테이너 확인 (실행/중지)
```shell
$ sudo docker ps -a 
```

## 모든 컨테이너 id만 확인 (실행/중지)
```shell
$ sudo docker ps -a -q
```

## 컨테이너 중지
```shell
$ sudo docker stop f6e513b399a5 # {containerId or containerName}
```

## 컨테이너 삭제
```shell
$ sudo docker rm f6e513b399a5 # {containerId or containerName}
```
- 중지가 된 상태에서만 삭제가 가능하다.

## 컨테이너 생성
```shell
$ sudo docker create -p 80:80 --name nx nginx
# a1s2d3f4
```
- 같은 이름의 컨테이너를 생성할 수 없다.

## 컨테이너 실행
```shell
$ sudo docker start a1s2d3f4 # containerId
# 127.0.0.1 확인
```
- 포트가 겹치면 실행되지 않는다.

## 컨테이너 중복 생성 및 실행
```shell
$ docker run -d -p 80:80 --name my-nginx nginx
$ docker run -d -p 88:80 --name my-nginx-2 nginx
$ docker ps
```

## 컨테이너 재실행
```shell
$ docker restart my-nginx
```