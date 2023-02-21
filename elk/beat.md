# 비트
## 비트 버전 문제
- 메트릭을 수집하고자하는 인스턴스에서 메트릭 비트 설치후 바로 es에 연결하도록 하고 실행했더니 버전 차이 문제가 발생하였다.
- 메트릭 비트 뿐만 아니라 모든 비트도 포함되는 것 같다.

```json
{
    "log.level":"error",
    "log.logger":"publisher_pipeline_output",
    "log.origin":{
        "file.name":"pipeline/client_worker.go",
        "file.line":150
    },
    "message":"Failed to connect to backoff(elasticsearch(http://host:9200)): Connection marked as failed becausethe onConnect callback failed: Elasticsearch is too old. Please upgrade the instance. If you would like to connectto older instances set output.elasticsearch.allow_older_versions to true. ES=8.0.0, Beat=8.4.0",
    "service.name":"metricbeat",
    "ecs.version":"1.6.0"
}
```
- message를 읽어보면
    - Elasticsearch is too old. Please upgrade the instance. If you would like to connect to older instances set output.elasticsearch.allow_older_versions to true. ES=8.0.0, Beat=8.4.0"
    - ES 버전은 8.0.0 이고 비트 버전은 8.4.0 이라고 한다.
    - ES 버전이 구데기라서 버전이 오래되어 에러가 발생
- 해결하는 방법은 두가지가 있다.
    1. ES 버전을 업한다.
    2. Beat 설정(XXbeat.yml)에 다음과 같이 옵션 값을 추가해준다.
```yaml
output.elasticsearch:
  allow_older_versions: true
```