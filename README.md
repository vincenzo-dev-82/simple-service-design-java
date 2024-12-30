# simple-service-design-java

---
## Project
### Version
```
- Spring-Boot 3.3.8
- Jdk 21
- Gradle 8.5
```
* 스프링부트 최신인 3.4에 대한 이슈가 있는 듯하여, 3.3의 GA인 3.3.6을 선택
    * Jdk17 ~ Jdk21 선택 가능
* Gradle build version 을 참고하여 8.5 를 선택
    * https://docs.gradle.org/current/userguide/compatibility.html

### 빌드 및 프로젝트 실행
```bash
./gradlew wrapper --gradle-version=8.5 --distribution-type=bin
./gradlew -v
./gradlew clean build -xtest
./gradlew bootRun
```

## Services
### Twitter
* ConcurrentHashMap을 사용하여 트윗과 팔로워를 관리해 본다.

### LRU
* LinkedHashMap을 사용해서 구현해 본다.
* 장점 
  * 코드 간결성 
  * LinkedHashMap의 내장 기능을 활용하여 직접 자료구조를 구현하지 않아도 됩니다.
  * removeEldestEntry 메서드를 오버라이드하여 LRU 정책을 간단히 적용.
  * 시간 복잡도 
    * get 및 put 연산: O(1). 
  * 자동 정렬 
    * accessOrder = true로 설정하면 최근 사용된 순서로 항목을 자동 정렬. 
* 단점 
  * 스레드 안전하지 않음 
  * 단일 스레드 환경에서는 문제가 없지만, 다중 스레드 환경에서는 동기화가 필요.
  * 다중 스레드 환경에서는 **Collections.synchronizedMap**으로 래핑하거나, 다른 스레드 안전한 대안을 고려해야 함.

### Producer Consumer
