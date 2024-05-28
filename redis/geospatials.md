# Geospatials
- 좌표를 저장, 검색할 수 있는 데이터 타입
  - Sorted Set Data Structure를 사용하기 때문에 몇 가지 GEO 명령은 Sorted Set의 명령어를 사용할 수 있다.
- 거리 계산, 범위 탐색 등 지원

<br>

## GEOADD
- 경도와 위도를 입력할 수 있다.

> GEOADD key longitude(경도/세로선) latitude(위도/가로선) member(city) [longitude latitude member ...]

- 경도(longitude)의 범위는 -180부터 180, 
- 위도(latitude)의 범위는 -85.05112878부터 85.05112878

```redis
$ GEOADD seoul:station 126.923917 37.556944 hongdae 127.027583 37.497928 gangnam
(integer) 2
```

## GEODIST
- 두 member(city)간 거리를 구할 수 있다.

> GEODIST key member1 member2 [unit]

unit : 단위는 m(meter), km(kilometer), mi(mile), ft(feet)가 있으며 기본 단위는 meter

```redis
$ GEODIST seoul:station hongdae gangnam KM
"11.2561"
```