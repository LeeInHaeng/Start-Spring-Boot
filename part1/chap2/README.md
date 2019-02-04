# SpringBoot���� JPA ���
- ������Ʈ ���� �� SQL �κп��� JPA�� MySQL üũ
- src/main/resources/application.properties ���� datasource�� ���õ� ���� �ۼ� (mysql version 5.7)
```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/jpa_ex?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.username=jpa_user
spring.datasource.password=jpa_user
```
- JPA ������̼��� ����� Ŭ���� �ۼ� (org.zerock.domain�� Board.java)
  - Entity : �ش� Ŭ������ ��ƼƼ Ŭ�������� ���
  - Table : Ŭ������� �ٸ� �̸����� ���̺� �̸��� ����
  - Id : Primary key�� �ǹ�
  - GeneratedValue : PK�� � �������� �����ϴ����� ���
  - CreationTimestamp, UpdateTimestamp : ��ƼƼ�� �����ǰų� ������Ʈ�Ǵ� ������ ��¥ �����͸� ���
- application.properties�� JPA ����
```
#��Ű�� ����(create)
spring.jpa.hibernate.ddl-auto=update
# DDL ���� �� �����ͺ��̽� ������ ����� ����ϴ°�?
spring.jpa.generate-ddl=false
# ����Ǵ� SQL���� ������ ���ΰ�?
spring.jpa.show-sql=true
# �����ͺ��̽��� ������ ����ϴ°�?
spring.jpa.database=mysql
# �α� ����
logging.level.org.hibernate=info
# MySQL �� ����
spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect
```
- Repository �������̽� ���� (org.zerock.persistence�� BoardRepository.java)
```
package org.zerock.persistence;

import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.Board;

public interface BoardRepository extends CrudRepository<Board, Long> {

}
```
- �ش� Repository �������̽� ���
```
	@Autowired
	private BoardRepository boardRepo;
	
	/*
	@Test
	public void testInsert() {
		Board board = new Board();
		board.setTitle("�Խù��� ����");
		board.setContent("�Խù� ���� �ֱ�....");
		board.setWriter("user00");
		
		boardRepo.save(board);
	}
	
	@Test
	public void testRead() {
		boardRepo.findById(1L).ifPresent((board)->{
			System.out.println(board);
		});
	}

	@Test
	public void testUpdate() {
		System.out.println("Read First..................................");
		boardRepo.findById(1L).ifPresent((board)->{
			
			System.out.println("Update Title.................................");
			board.setTitle("������ �����Դϴ�.");
			
			System.out.println("Call Save().....................................");
			boardRepo.save(board);
			
		});
		
	}
	*/
	
	@Test
	public void testDelete() {
		System.out.println("delete entity");
		
		boardRepo.deleteById(1L);
	}
```