# 도커 명령어들

## 컨테이너 내부 셀 실행
```shell
$ sudo docker exec -it tc /bin/bash # tc -> --name 으로 붙여준 컨테이너 이름
```
- `-it`: input terminal, 터미널을 오픈하는 옵션

<br>

## 컨테이너 로그 확인
```shell
$ docker logs tc # stdout, stderr
```

<br>

## 호스트 및 컨테이너 간 파일 복사
```shell
# 현재 로컬 시스템에서 다른 컨테이너로 복사
$ docker cp <local path> <to container>:<to path>
# echo test1234 > test.txt
# docker cp test.txt tc:/
# sudo docker exec -it tc cat /test.txt

# 컨테이너에서 현재 로컬시스템으로 복사
$ docker cp <container>:<local path> <path>
# docker cp tc:/test.txt ./test2.txt

# 컨테이너에서 다른 컨테이너간 복사
$ docker cp <from container>:<path> <to container>:<path>
```

<br>

## 임시 컨테이너 생성
```shell
$ docker run -d -p 80:8080 --rm --name tc tomcat
```
- 컨테이너 생성 및 실행 후에 컨테이너 종료시 바로 컨테이너가 삭제가 된다.

<br>

## 실행중인 도커 컨테이너 모두 중지
```shell
$ docker stop `docker ps -a -q`
```

<br>

## 도커 컨테이너 모두 삭제
```shell
$ docker rm `docker ps -a -q`
```

<br>

## 도커 이미지 모두 삭제
```shell
$ docker rmi `docker images -q` 
```

<br>

## 환경 변수 사용해 데이터 전달
```shell
$ docker run -d --name nx -e env_name=test1234 --rm nginx
$ docker exec -it nx bash
$ printenv env_name
```
- `-e {key}={value}` : 환경 변수 (env) 전달

<br>

## mysql 다운 및 실행
```shell
$ docker run --name test-mysql -e MYSQL_ROOT_PASSWORD='!qhdkscjfwj@' -d mysql
# MYSQL_ROOT_PASSWORD는 mysql 진영에서 정해진 환경변수 이름

$ docker exec -it test-mysql mysql -u root -p 
# password: !qhdkscjfwj@
```
