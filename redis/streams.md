# Streams
- append only log 에 consumer groups 기능을 더한 자료구조
  - 다수의 컨슈머에서 안전하게 메시지를 컨슈밍할 수 있도록
  - **컨슈머가 동일한 메시지를 여러번 처리하는 경우가 발생할 수 있는데 컨슈머 그룹을 통해 동일한 메시지에 대해 중복 처리를 해결**
- 스트림에 추가되는 메시지는 고유 아이디를 갖는다.
  - 유니크 id를 통해 데이터를 읽을 때 O(1)의 시간복잡도
  - 스트림에 추가되는 시간과 순서를 기준으로 id를 할당받는다.
  - ID는 두 개의 숫자로 구성된다. <millisecondsTime>-<sequenceNumber>
- 좋아요 기능에 활용할 수 있다.

#### append only log
- DB나 분산 시스템에 사용되는 저장 알고리즘
  - 수정되거나 삭제되지 않고 추가만 되는 구조
- AOF (Append Only File) 도 Append Only Log의 일종이다.


<br>

## XADD
- 스트림에 데이터를 추가하는 명령어
- 해시와 같은 key : value 형태
```redis
$ XADD events * action like user_id 1 product_id 1
"1716860214498-0"

$ XADD events * action like user_id 2 product_id 1
"1716860417066-0"
```
- stream : events 라는 스트림 생성
- key : action
- `*` : key 이후 * 옵션을 주면 자동으로 유니크한 id 할당

## XRANGE
- 리스트와 유사하게 다수의 메시지를 조회
- -+는 해당 키(스트림)의 모든 데이터를 조회
- id를 지정해서 조회할 수도 있다.
```redis
-- XRANGE key start end
$ XRANGE events - +

$ XRANGE events 1538319053569-10 1538319053569-99
```

## XDEL
- 스트림에서 제거
```redis
-- XDEL events {id}
$ XDEL events 1716860417066-0
```