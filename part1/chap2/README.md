# SpringBoot에서 JPA 사용
- 프로젝트 생성 시 SQL 부분에서 JPA와 MySQL 체크
- src/main/resources/application.properties 에서 datasource와 관련된 설정 작성 (mysql version 5.7)
```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/jpa_ex?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.username=jpa_user
spring.datasource.password=jpa_user
```
- JPA 어노테이션을 사용한 클래스 작성 (org.zerock.domain의 Board.java)
  - Entity : 해당 클래스가 엔티티 클래스임을 명시
  - Table : 클래스명과 다른 이름으로 테이블 이름을 지정
  - Id : Primary key를 의미
  - GeneratedValue : PK를 어떤 전략으로 생성하는지를 명시
  - CreationTimestamp, UpdateTimestamp : 엔티티가 생성되거나 업데이트되는 시점의 날짜 데이터를 기록
- application.properties에 JPA 설정
```
#스키마 생성(create)
spring.jpa.hibernate.ddl-auto=update
# DDL 생성 시 데이터베이스 고유의 기능을 사용하는가?
spring.jpa.generate-ddl=false
# 실행되는 SQL문을 보여줄 것인가?
spring.jpa.show-sql=true
# 데이터베이스는 무엇을 사용하는가?
spring.jpa.database=mysql
# 로그 레벨
logging.level.org.hibernate=info
# MySQL 상세 지정
spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect
```
- Repository 인터페이스 설계 (org.zerock.persistence의 BoardRepository.java)
```
package org.zerock.persistence;

import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.Board;

public interface BoardRepository extends CrudRepository<Board, Long> {

}
```
- 해당 Repository 인터페이스 사용