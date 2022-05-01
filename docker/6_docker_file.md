# 도커 파일
- 도커 이미지 생성을 위한 스크립트
- Dockerfile에 나열된 명령문을 차례대로 수행 후 이미지 생성
- Dockerfile을 작성 할 땐 실제 파일의 이름을 'Dockerfile'로 해야한다.
  - 대소문자 구별하지 않는다.

```dockerfile
# 새로 만들 이미지는 python:3.7 이미지를 사용
FROM python:3.7

# Dockerfile 작성자
MAINTAINER OHTAEG <otk1090@naver.com> 

RUN mkdir /echo
COPY test_server.py /echo

CMD ["python", "/echo/test_server.py"]
```
- `FROM {Image Name}`
  - FROM은 항상 Dockerfile의 맨 첫줄에 온다.
  - 생성할 이미지의 기반이 되는 이미지 이름을 지정
- `RUN {명령어}`
  - 이미지를 만들 때 실행
  - RUN 명령을 실행할 때 마다 레이어가 생성되고 캐시된다.
- `COPY`
  - build 명령 중간에 호스트의 파일 또는 폴더를 이미지에 가져오는 것
- `CMD`
  - 컨테이너 생성, 실행할 때 실행되는 명령어
  - CMD [“executable”,”param1”,”param2”]


<br>

## 도커파일 빌드
- 만든 Dockerfile을 Image로 빌드한다.
```docker
# docker build -t [이미지 이름:이미지 버전] [Dockerfile의 경로]
# 현재 디렉토리
$ docker build -t ehco_test .
```

<br>

- 172.17.0.1
  - 도커 전용 인터페이스
