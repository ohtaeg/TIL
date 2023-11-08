# Object header의 biased lock 이 lock counter 일까?
- Object의 Header에는 Lock Counter를 가지고 있는데 Lock을 획득하면 1증가, 놓으면 1감소한다. 
  - Lock을 소유한 Thread만 가능하다. Thread는 한번 수행에 한번의 Lock만을 획득하거나 놓을 수 있다. 
- Count가 0일때 다른 Thread가 Lock을 획득할 수 있고 Thread가 반복해서 Lock을 획득하면 Count가 증가한다. 
- Critical Section은 Object Reference와 연계해 동기화를 수행한다. 
- Thread는 Critical Section의 첫 Instruction을 수행할 때 참조하는 Object에 대해 Lock을 획득해야 한다.
- Critical Section을 떠날 때 Lock은 자동 Release되며 명시적인 작업은 불필요하다.
- JVM을 이용하는 사람들은 단지 Critical Section을 지정해주기만 하면 동기화는 자동으로 된다는 것이다.
- 두번째 header는 Object의 경우 Method Table을 위한 Pointer를 가지고 있게 되고 Array의 경우는 Array 엔트리 개수 정보를 가지고 있게 된다.
- 세번째 header는 lock 정보를 저장한다. 
  - Fat Lock mode라면 Monitor Index를 위해 23 bit가 할당되고 Thin Lock의 경우 15 bits의 Thread Index정보와 Lock count를 저장한다.