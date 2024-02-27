# Lists
- String을 Linked List로 저장
  - 각 노드가 이전노드와 다음 노드 포인트를 가리키고 있는 더블 링크드 리스트이다.
- 링크드 리스트 특성에 맞게 양 끝에 데이터를 push / pop에 최적화 O(1)
- 레디스 리스트를 활용한다면 큐 or 스택을 쉽게 구현할 수 있다.
  - `LPUSH` 명령어를 통해 리스트의 왼쪽부터 추가 가능하기 떄문에 FIFO 큐 구현 가능
  ```redis
    -- 큐처럼 사용하기
    -- [3 2 1]
    $ LPUSH queue 1 2 3
    -- [3, 2] 
    $ RPOP queue
  ```
  - `LPOP` 명령어를 통해 리스트의 왼쪽부터 pop 가능하기 때문에 FILO 스택 구현 가능
  ```redis
    -- 스택 처럼 사용하기 
    -- [3 2 1]
    $ LPUSH stack 1 2 3
  
    -- [2, 1]
    $ LPOP stack
  ```
  

## LRANGE
- list에서 다수의 value들을 조회할 수 있다.
- index(0, 1, 2...)를 이용하며, 왼쪽 기준으로 오른쪽으로 접근하는데 왼쪽부터 0부터 증가하는 방식,
- 오른쪽기준으로 왼쪽으로 접근하는 경우는 맨 오른쪽은 -1 부터 시작하며 -2, -3 과 같이 점차 인덱스를 감소시키며 탐색하면된다.
  - $ LRANGE {start} {end}
```redis
--   [0]  [1]  [2]
--   [-3] [-2] [-1]
--   [1   2    3  ]

$ RPUSH list 1 2 3

-- [1 2 3]
$ LRANGE list 0 2

-- [2 3]
$ LRANGE list 1 2

-- [1 2 3]
$ LRANGE list -3 -1

-- [1 2]
$ LRANGE list -3 -2

-- 첫번째 아이템부터 마지막 아이템까지
-- [1 2 3]
$ LRANGE list 0 -1

-- (empty array)
$ LRANGE list -1 -3
```  

## LTRIM
- $ LTRIM {start} {end}
- start Index 부터 end Index의 사이의 value들만 **남기고 나머지 삭제**

```redis
--   [ 1 2 3 ]
$ RPUSH list 1 2 3

-- [1]
$ LTRIM list 0 0

--   [ 1 2 3 ]
$ RPUSH list 2 3

-- [1 2]
$ LTRIM list 0 1
```