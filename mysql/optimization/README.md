# Mysql 성능 최적화
- 테스트를 위한 로컬 세팅
```sh
$ docker pull mysql:8.2.0

$ docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=1234 -d -p 3306:330
6 mysql:8.2.0

# docker 명령어 단축
$ vi ~/.zshrc

alias dme='docker exec -it mysql-container bash'
alias drm='docker restart mysql-container'

$ source ~/.zshrc

# $ dme
$ docker exec -it mysql-container mysql -u root -p

# mysql 접속 후
$ CREATE DATABASE test;

$ docker stop mysql-container

# $ drm
$ docker restart mysql-container 
```