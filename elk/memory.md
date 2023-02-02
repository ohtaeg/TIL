# 메모리 설정
## 권장 힙 메모리 할당하기
- ES는 JVM을 활용하기에 heap을 사용한다.
- 메모리 관련해서 [`공식 문서`](https://www.elastic.co/guide/en/elasticsearch/reference/current/advanced-configuration.html#set-jvm-heap-size) 에서 권장하는 사항은 다음과 같다.
1. **기본적으로 heap size의 MIN, MAX 값은 같게 맞춘다. (-Xms 랑 -Xmx)**
```text
To override the default heap size, set the minimum and maximum heap size settings, Xms and Xmx. 
The minimum and maximum values must be the same.
```

2. **최소/최대 힙 메모리 값을 해당 서버의 메모리의 50% 이상으로 설정하지 마라**
- `Set Xms and Xmx to no more than 50% of your total memory`
- 그 이유는 ES에는 JVM 힙 이외의 목적으로 메모리가 필요하다고 한다. 
- 예를 들어 ES는 효율적인 네트워크 통신을 위해 힙 버퍼를 사용하고 파일에 대한 효율적인 액세스를 위해 운영 체제의 파일 시스템 캐시에 의존한다.


3. **최소/최대 힙 메모리 값을 Compressed OOPS 이상으로 설정하지 말라**
- ES는 **서버가 가진 메모리의 절반을 힙사이즈로 설정하는 것을 권장한다.**
- 즉, 서버 메모리가 64GB 이상의 메모리를 가지고 있다면 32GB를 힙메모리로 설정하는 것을 권장한다.
- [공식 문서](https://www.elastic.co/guide/en/elasticsearch/reference/current/advanced-configuration.html#set-jvm-heap-size)에서도 나와있듯이 26GB ~ 30GB를 넘기지 않도록 하는게 시스템을 운영하는데 안정적이라고 한다.
```text
Set Xms and Xmx to no more than the threshold for compressed ordinary object pointers (oops).
The exact threshold varies but 26GB is safe on most systems and can be as large as 30GB on some systems.
```