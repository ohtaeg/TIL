# 메트릭 비트

## 메트릭 비트 실행시 권한 에러
- 시스템의 메트릭 정보를 모니터링하기 위해 메트릭 비트를 설치하였고, 그 과정에서 다음과 같은 권한 에러를 만났다.

```json
{
    "log.level":"error",
    "log.origin":{
        "file.name":"instance/beat.go",
        "file.line":1051
    },
    "message":"Exiting: loading configs: 1 error: invalid config: config file (\"/metricbeat/modules.d/system.yml\") must be owned by the user identifier (uid=0) or root",
    "service.name":"metricbeat",
    "ecs.version":"1.6.0"
}
```

- 찾아보니 권한에러인데 [공홈](https://www.elastic.co/guide/en/beats/libbeat/8.0/config-file-permissions.html)에서 해결책을 알려준다.
- 모든 비트들의 설정은 ownership 과 permission을 체크하는데 이유가
  **권한이 없는 사용자가 Beat에서 실행되는 구성을 제공하거나 수정하는 것을 방지하기 위한 것**이라고 한다.
- 해결 방법은 2가지가 있다.
    1. 메트릭 비트 관련 권한 및 소유자를 변경해준다.
        - 소유자를 root로 변경해준다. 메트릭 비트 폴더 통째로 변경하거나 변경하고 싶은 폴더만 지정해준다.
            - `sudo chown -R root /User/Taegyeoung/desktop/metricbeat-poc/metricbeat.yml`
        - 읽기 쓰기 권한을 변경해준다.
            - `sudo chmod -R 0644 /User/Taegyeoung/desktop/metricbeat-poc/metricbeat.yml`
        - 공홈에서는 다음과 같이 하라고도 나와있다.
            - `chmod go-w /etc/{beatname}/{beatname}.yml`
    2. 메트릭비트 실행시 권한 체크를 무시하는 커맨드 옵션을 추가해준다.
        - `sudo ./metricbeat -e -strict.perms=false`
- yml 파일 뿐만 아니라 module 관련해서도 권한 및 소유자를 설정해줘야한다.

<br>

# 메트릭 비트 - 몽고 디비 연결 문제
- 몽고디비 Atlas와 연결하면서 에러를 조우하였다.
- 원래 몽고디비 컴파스를 통해 연결되는 URL은 다음과 같다.
  - `mongodb+srv://~~~~~` 
- 위 URL 그대로 설정하고나서 처음 만난 에러는 다음과 같다.
    - `too many colons in address`
- 찾아보니 [공홈](https://www.elastic.co/guide/en/beats/metricbeat/current/metricbeat-module-mongodb.html)에서는 다음과 같은 포맷으로 입력해야한다고 한다.
  - `[mongodb://][user:pass@]host[:port][?options]`
- 즉 몽고디비 컴파스와 연결되는 URL과 공홈에서 연결하는 포맷이 달라 발생하던 에러로 간주가 된다.
  - `mongdb+srv://` 를 `mongdb://` 로 변경하는 작업을 거쳐야한다.
- 변경했던 작업은 추후 정리한다.
    