# 엘라스틱 서치 hot - warm - cold 아키텍처

- https://www.elastic.co/kr/blog/implementing-hot-warm-cold-in-elasticsearch-with-index-lifecycle-management
- https://dol9.tistory.com/275
- 회사에서 ES도 다루고 있는데 초반에는 별다른 관리 없이도 문제가 없었는데 인입되는 양이 점점 많아지면서 인덱스 관리가 필요하여 해당 아키텍처를 고려


## Hot, Warm, Cold란?

- index의 사용빈도 (즉, disk의 I/O 빈도) 에 따라 tier를 나눠서 보관하는 방법이다.
- hot : 로그는 가장 많이 검색됩니다
    - hot 데이터 노드는 최신 인덱스들을 저장하며, 클러스터의 모든 색인 부하를 처리
    - 최신 데이터에 대한 쿼리가 가장 많기 때문에 이 노드는 일반적으로 IO 성능이 중요하여 SSD를 사용
    - hot 노드의 롤 오버 인덱스 조건에 충족하는 경우 warm 노드로 이전
        - 예로 hot 노드 보존 기간을 초과하거나, 용량이 30GB 도달하거나 문서가 백만개 이상 넘은 경우
- warm : 로그는 검색되기는 하지만 자주 검색되지는 않습니다
    - warm 노드는 비교적 가끔 읽기 전용으로써 인덱스의 장기간 저장을 처리하기 위해 HDD를  사용
    - 만약 hot 노드에서 샤드를 여러개로 분할해서 저장했다면 해당 단계에서 샤드를 병합한다.
- cold : 검색 빈도가 낮지만 만약의 경우를 위해  유지
    - 조회될 경우가 거의 없지만 삭제할 수 없는 데이터는 snapshot을 만들어서 S3 글래셔 등 NAS 등에 보관
    - 만약 hot 노드에서 샤드를 여러개로 분할해서 저장했다면 해당 단계에서 샤드를 1개로 축소시킨다.

- hot 노드에는 더 많은 CPU 리소스와 더 빠른 IO가 필요하며, Warm 및 cold 노드에는 일반적으로 노드당 더 많은 디스크 공간이 필요하지만 CPU는 더 적어도 되고 IO는 더 느려도 괜찮습니다.



## Hot, Warm, Cold 노드 구성 방법

```json
PUT _ilm/policy/hot-warm-cold-delete-60days
{
  "policy": {
    "phases": {
      "hot": {
        "actions": {
          "rollover": {
            "max_size":"50gb",
            "max_age":"30d"
          },
          "set_priority": {
            "priority": 50
          }
        }
      },
      "warm": {
        "min_age": "7d",
        "actions": {
          "forcemerge": {
            "max_num_segments": 1
          },
          "shrink": {
            "number_of_shards": 1
          },
          "allocate": {
            "require": {
              "data": "warm"
            }
          },
          "set_priority": {
            "priority": 25
          }
        }
      },
      "cold": {
        "min_age": "30d",
        "actions": {
          "set_priority": {
            "priority": 0
          },
          "freeze": {},
          "allocate": {
            "require": {
              "data": "cold"
            }
          }
        }
      },
      "delete": {
        "min_age": "60d",
        "actions": {
          "delete": {}
        }
      }
    }
  }
}
```



- 각 노드마다 elasticsearch.yml 설정 파일에서 *ILM* 옵션을 사용하여 hot, warm, cold를 담당하는 노드를 구성한다.
    - 혹은 키바나에서 바로 설정이 가능하다.
- hot 단계는 다른 노드들보다 인덱스 우선순위를 높은 값으로 설정하여 샤드 재할당, 롤 오버 등 먼저 복구되도록 한다.
- 롤 오버 작업은 각 인덱스의 크기 또는 기간을 관리하는데 사용한다.
    - 예롷 30일 후 인덱스 크기가 50GB에 도달하면 새 인덱스를 생성하고 재할당할 것인지 등 설정 가능
    - 이때 새로 만들어진 인덱스는 다시 롤 오버 정책이 적용되고 롤 오버된 기존 인덱스는 최대 7일 기다렸다가 warm 단계로 진입한다.
- warm 단계는 hot보다 낮은 인덱스 우선순위로  설정하여 warm 노드로 옮겨진다. 이동 완료 후 30일 후 롤 오버되어 cold 단계로 진입한다.
- cold 단계는 warm 단계보다 인덱스 우선순위를 낮춰 cold 단계로 옮겨진다. 이동 완료 후 60일 후 delete 단계로 진입한다.