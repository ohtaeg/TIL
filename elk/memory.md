# 메모리 설정
## 권장 힙 메모리 할당하기
- ES는 JVM을 활용하기에 heap을 사용한다.
- 메모리 관련해서 [`공식 문서`](https://www.elastic.co/guide/en/elasticsearch/reference/current/advanced-configuration.html#set-jvm-heap-size) 에서 권장하는 사항은 다음과 같다.
1. 기본적으로 heap size의 MIN, MAX 값은 같게 맞춘다. (-Xms 랑 -Xmx)
```text
To override the default heap size, set the minimum and maximum heap size settings, Xms and Xmx. 
The minimum and maximum values must be the same.
```