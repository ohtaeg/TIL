## 도커 설치
- OS : Ubuntu 20.04
```shell
# 시스템 정보 확인
$ uname -a
# Linux {private ip} 5.11.0-1028-aws #31~20.04.1-Ubuntu SMP Fri Jan 14 14:37:50 UTC 2022 x86_64 x86_64 x86_64 GNU/Linux
```

<br>

- 관리자 권한으로 넘어가서 수행 (optional)
```shell
# ubuntu -> root~#   
# 유저뒤에 #이 붙으면 관리자 권한임을 뜻한다.
$ sudo -i
```

<br>

- apt (패키지 매니저) update 후 docker install
```shell
$ apt update
$ apt install docker.io
```

<br>

- 원하는 서비스 도커에서 검색 ex) tomcat
````shell
$ docker search tomcat
````

<br>

- 실행
```shell
$ docker run -d -p 8080:8080 --name consol/tomcat-7.0:latest
```
- `-d`: 백그라운드 실행
- `-p 8080:8080` : 독립된 환경에서의 서비스 네트워크를 밖으로 노출 시켜야할때 사용
    - 8080:8080 : 8080 서비스 포트를 외부 8080프토로 연결해서 뚫어준다.
- `--name tc` : tc라는 이름으로 네이밍

