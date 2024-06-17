# Sets
- 중복없이 문자열 저장 및 정렬되지 않는 집합
- Set에 대한 여러 연산이 존재한다.

## SADD
- set에 추가하는 커맨드 
```redis
$ SADD user:1:fruits apple banana orange orange
```

<br>

## SMEMBERS
- set에 있는 모든 아이템 출력

```redis

$ SADD user:1:fruits apple banana orange orange

-- [apple, banana, orange]
$ SMEMBERS user:1:fruits
```

<br>

## SCARD
- set에 카디널리티를 출력
- 카디널리티 : 고유한 아이템 갯수
```redis

$ SADD user:1:fruits apple banana orange orange

-- [apple, banana, orange]
$ SMEMBERS user:1:fruits

-- [3]
$ SCARD user:1:fruits
```

<br>

## SISMEMBER
- set에 특정 아이템이 포함되었는지 확인하는 커맨드

```redis

$ SADD user:1:fruits apple banana orange orange

-- 1
$ SISMEMBER user:1:fruits banana

-- 0
$ SISMEMBER user:1:fruits BANANA
```

<br>

## SINTER
- 두 set이 갖고 있는 아이템들 중 `교집합`을 구한다.
```redis

$ SADD user:1:fruits apple banana orange
$ SADD user:2:fruits apple lemon

-- [apple]
$ SINTER user:1:fruits user:2:fruits 
```

<br>

## SDIFF
- 두 set이 갖고 있는 아이템들 중 `차집합`을 구한다.
```redis

$ SADD user:1:fruits apple banana orange
$ SADD user:2:fruits apple lemon

-- [banana orange]
$ SDIFF user:1:fruits user:2:fruits 

-- [lemon]
$ SDIFF user:2:fruits user:1:fruits 
```
## SUNION
- 두 set이 갖고 있는 아이템들 중 `합집합`을 구한다.
```redis

$ SADD user:1:fruits apple banana orange
$ SADD user:2:fruits apple lemon

-- [apple banana orange lemon]
$ SUNION user:1:fruits user:2:fruits 
```

## 장바구니
- 장바구니 데이터는 수시로 변경될 수 있고, 결제까지 가지 않을 수 있다.
- Set을 이용하여 임시 데이터성으로, 그리고 중복 데이터 관리가 쉬어진다.