# Sentinel 
## Start
```docker
$ docker-compose up --scale redis-sentinel=3 -d 
```

## Redis master-slave
- 총 3개의 레디스 컨테이너, 1 master - 2 slave
```docker
# redis master
$ docker exec -it redis bin/bash
$ redis-cli
127.0.0.1:6379> role
1) "master"
```


```docker
# redis slave-1 or redis slave-2 
$ docker exec -it redis-slave-1 bin/bash
$ redis-cli
127.0.0.1:6379> role
1) "slave"
```

<br>

## Redis sentinel
- 총 3개의 레디스 센티넬 컨테이너
```docker
# redis sentinel
$ docker exec -it spring-redis-sentinel-redis-sentinel-1 /bin/bash
$ redis-cli -p 26379
127.0.0.1:26379> info sentinel
sentinel_masters:1
master0:name=master,status=ok,address=172.22.0.2:6379,slaves=2,sentinels=3
```