# 회원과 프로필 사진 관계 JPA 처리 (단방향 ManyToOne, Join 이용)
![123](https://user-images.githubusercontent.com/20277647/52330992-4ecc6a80-2a3a-11e9-9712-b71c9172c2f9.PNG)
- 회원을 나타내는 Member 엔티티 클래스와, 프로필을 나타내는 Profile 엔티티 클래스를 작성
- 프로필 에서는 회원 ID 인 외래키가 있으므로 Profile 클래스에 Member 객체 선언
- 여기서 주의할 점은 단방향 이기 때문에 한쪽 클래스에서만 선언한다.
```
// Profile.java
@Entity
@Table(name="tbl_profile")
public class Profile {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long fno;
	private String fname;
	private boolean current;
	private Member member;
	...
}
```
- JPA를 이용해 연관관계 처리
  - OneToOne, OneToMany, ManyToOne, ManyToMany 어노테이션을 이용
```
// Profile.java
	@ManyToOne
	private Member member;
```
- JPA를 이용해 Join 처리
  - 생성된 DDL을 살펴보면 외래키는 존재하지만, 공통된 칼럼명이 없기 때문에 외부조인을 사용
  - LEFT OUTER JOIN을 이용하여 JPQL 사용
- uid가 user1 인 회원의 정보와 더불어 회원의 프로필 사진 개수를 알고 싶은 경우
```
// MemberRepository.java
	@Query("SELECT m.uid, count(p) FROM Member m LEFT OUTER JOIN Profile p " + 
	"ON m.uid=p.member WHERE m.uid=?1 GROUP BY m")
	public List<Object[]> getMemberWithProfileCount(String uid);

// ProfileTests.java
	@Test
	public void testFetchJoin1() {
		List<Object[]> result = memberRepo.getMemberWithProfileCount("user1");
		
		result.forEach(arr -> 
				System.out.println(Arrays.toString(arr)));
	}
```
- 회원 정보와 현재 사용 중인 프로필에 대한 정보를 얻고 싶은 경우
```
// MemberRepository.java
	@Query("SELECT m, p FROM Member m LEFT OUTER JOIN Profile p " + 
	"ON m.uid=p.member WHERE m.uid=?1 AND p.current=true")
	public List<Object[]> getMemberWithProfile(String uid);
```

# 자료실과 첨부 파일의 관계 (단방향 OneToMany, Join 이용)
![123](https://user-images.githubusercontent.com/20277647/52335378-9b697300-2a45-11e9-9e58-49c0df594392.PNG)
- 자료를 나타내는 PDSBoard 엔티티 클래스와, 첨부 파일을 나타내는 PDSFile 엔티티 클래스를 작성
- OneToMany 예제를 위해 Board 클래스에서 File 클래스의 객체 선언
- 여기서 주의할 점은 단방향 이기 때문에 한쪽 클래스에서만 선언한다.
- 또한 OneToMany의 경우 JoinTable이나 JoinColumn 어노테이션을 함께 선언해야 한다.
- 그리고 2개의 테이블에 한번에 데이터 삽입을 위해 Cascade 설정을 이용한다.
```
// PDSBoard.java
@Entity
@Table(name="tbl_pds")
public class PDSBoard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pid;
	private String pname;
	private String pwriter;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="pdsno")
	private List<PDSFile> files;
	...
}
```
- 자료와 첨부 파일의 수를 자료 번호의 역순으로 출력하는 예
```
	@Query("SELECT p, count(f) FROM PDSBoard p LEFT OUTER JOIN p.files f " + 
			"WHERE p.pid>0 GROUP BY p ORDER BY p.pid DESC")
	public List<Object[]> getSummary();
```

# JPQL을 이용하여 DML 처리
- Query 어노테이션은 기본적으로 select 구문만 지원하지만, Modifying 어노테이션을 이용하면 DML 작업 처리 가능
- UPDATE 문
```
	@Modifying
	@Query("UPDATE FROM PDSFile f set f.pdsfile = ?2 WHERE f.fno = ?1")
	public int updatePDSFile(Long fno, String newFileName);
```
- DELETE 문
```
	@Modifying
	@Query("DELETE FROM PDSFile f where f.fno=?1")
	public int deletePDSFile(Long fno);
```

# 게시물과 댓글의 관계 (양방향 예제)
![123](https://user-images.githubusercontent.com/20277647/52397879-6c610900-2afa-11e9-8dfc-00e94ab5e66e.PNG)
- 게시물을 나타내는 FreeBoard 엔티티 클래스와, 댓글을 나타내는 FreeBoardReply 엔티티 클래스를 작성
- 게시물 입장에서는 댓글이 일대다 관계이므로 OneToMany로 설정
- 댓글 입장에서는 게시물이 다대일 관계이므로 ManyToOne으로 설정
- 양방향 이기 때문에 어느쪽이 PK가 되고, 어느쪽이 FK가 되는지를 명시해 주어야 한다.
- PK 쪽이 mappedBy라는 속성을 이용해서 자신이 다른 객체에게 매여있다는 것을 명시한다. (OneToMany 쪽)
  - ManyToOne쪽 DB 테이블을 살펴보면 칼럼이 추가 된다.
```
// FreeBoard.java
...
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long bno;
	private String title;
	private String writer;
	private String content;
	
	@CreationTimestamp
	private Timestamp regdate;
	@CreationTimestamp
	private Timestamp updatedate;
	
	@OneToMany(mappedBy="board", cascade=CascadeType.ALL)
	private List<FreeBoardReply> replies;
...

// FreeBoardReply.java
...
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rno;
	private String reply;
	private String replyer;
	
	@CreationTimestamp
	private Timestamp replydate;
	@CreationTimestamp
	private Timestamp updatedate;
	
	@ManyToOne
	private FreeBoard board;
...
```
- 각 엔티티 클래스에 맞는 Repository 인터페이스 작성
- Repository를 이용해서 간단한 데이터 삽입
  - ManyToOne쪽 DB 테이블을 살펴보면 (FK)칼럼이 추가 되었기 때문에 이부분도 set으로 셋팅 (bno)
```
	@Test
	public void insertReply1Way() {
		FreeBoard board = new FreeBoard();
		board.setBno(199L);
		
		FreeBoardReply reply = new FreeBoardReply();
		reply.setReply("REPLY..........");
		reply.setReplyer("replyer00");
		reply.setBoard(board);
		
		replyRepo.save(reply);
	}
```
- Join과 Paging 사용 예제
```
public interface FreeBoardRepository extends CrudRepository<FreeBoard, Long> {

	@Query("SELECT b.bno, b.title, count(r) FROM FreeBoard b "+
			"LEFT OUTER JOIN b.replies r "+
			"WHERE b.bno > 0 GROUP BY b")
	public List<Object[]> getPage(Pageable page);
}

// 사용
	@Test
	public void testList3() {
		Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "bno");
		
		boardRepo.getPage(page).forEach(arr -> System.out.println(Arrays.toString(arr)));
	}
```

# JPA에서 인덱스 처리
- 인덱스 이해 : https://hashcode.co.kr/questions/1551/%EC%99%9C-db-%ED%85%8C%EC%9D%B4%EB%B8%94%EC%9D%98-%EB%AA%A8%EB%93%A0-%EC%BB%AC%EB%9F%BC%EC%97%90-%EC%9D%B8%EB%8D%B1%EC%8A%A4%EB%A5%BC-%EA%B1%B8%EB%A9%B4-%EC%95%88%EB%90%98%EB%82%98%EC%9A%94
- 테이블 생성 시 인덱스가 설계되도록 지정 (엔티티 클래스)
```
@Entity
@Table(name="tbl_free_replies", indexes = {@Index(unique=false, columnList="board_bno")} )
```