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

<br>
<br>

## Memory Swapping 비활성화

- [공식문서](https://www.elastic.co/guide/en/elasticsearch/reference/current/setup-configuration-memory.html)에 나와있듯이 OS는 파일 시스템 캐시에 가능한 한 많은 메모리를 사용하고
  사용하지 않는 응용 프로그램 메모리를 적극적으로 교체한다고 한다.
- 즉, **힙의 일부 또는 실행 가능한 페이지가 디스크로 교체될 수 있습니다.**
- ES는 메모리에 많은양의 데이터를 올려놓고 searching, indexing 등의 작업을 하기 때문에
- Swap을 켜 놓으면 disk로 내려서 작업이 되어 버리는 순간 성능에 악영향을 끼칠수 있다고 한다.
- 우선 현재 가동중에 swap을 끄는 커맨드는 다음과 같다. swap을 꺼보자.

```bash
$ sudo swapoff -a
```
- 해당 커멘드는 재부팅되면 사라지므로 영구적으로 적용하려면 다음과 같이한다.


```bash
$ sysctl -a | grep swappiness

or

$ sysctl vm.swappiness

vm.swappiness = 60
```
1. /etc/fstab 파일을 vim로 열어서 swap 관련된 내용을 주석 혹은 삭제한다.
2. sysctl의 vm.swappiness 옵션 값을 확인한다.

- 만약 vm.swappiness 값이 1이 아니거나 결과값이 없다면 1로 설정해준다.
- 60은 시스템 기본 값
- [공식 문서](https://www.elastic.co/guide/en/elasticsearch/reference/current/setup-configuration-memory.html)에서 확인할 수 있다.

```
Configure swappinessedit
Another option available on Linux systems is to ensure that the sysctl value vm.swappiness is set to 1.
This reduces the kernel’s tendency to swap and should not lead to swapping under normal circumstances, while still allowing the whole system to swap in emergency conditions.
```

```shell
$ sudo vi /etc/sysctl.conf

# 만약 vi를 통해 vm.swapiness 설정이 없고 60 기본값으로 되어있다면
# 기본값은 시스템 설정이므로 vm.swappiness 설정이 별도로 없을 수 있따.
# 새로 추가해주어야한다.

i # insert

vm.swappiness=1

:wq

$ sudo sysctl -p

# sysctl.conf 파일 수정 후 다시 로드하도록 하기 위한 커맨드
```

- `vm.swapiness 설정은 swap 사용의 적극성 혹은 활용 수준을 정하는 커널 속성`
- 숫자가 높을 수록 적극적으로 swap을 사용하겠다는 것이고, 낮을수록 활용하지 않겠다는 것이다.