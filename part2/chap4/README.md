# ȸ���� ������ ���� ���� JPA ó�� (�ܹ��� ManyToOne, Join �̿�)
![123](https://user-images.githubusercontent.com/20277647/52330992-4ecc6a80-2a3a-11e9-9712-b71c9172c2f9.PNG)
- ȸ���� ��Ÿ���� Member ��ƼƼ Ŭ������, �������� ��Ÿ���� Profile ��ƼƼ Ŭ������ �ۼ�
- ������ ������ ȸ�� ID �� �ܷ�Ű�� �����Ƿ� Profile Ŭ������ Member ��ü ����
- ���⼭ ������ ���� �ܹ��� �̱� ������ ���� Ŭ���������� �����Ѵ�.
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
- JPA�� �̿��� �������� ó��
  - OneToOne, OneToMany, ManyToOne, ManyToMany ������̼��� �̿�
```
// Profile.java
	@ManyToOne
	private Member member;
```
- JPA�� �̿��� Join ó��
  - ������ DDL�� ���캸�� �ܷ�Ű�� ����������, ����� Į������ ���� ������ �ܺ������� ���
  - LEFT OUTER JOIN�� �̿��Ͽ� JPQL ���
- uid�� user1 �� ȸ���� ������ ���Ҿ� ȸ���� ������ ���� ������ �˰� ���� ���
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
- ȸ�� ������ ���� ��� ���� �����ʿ� ���� ������ ��� ���� ���
```
// MemberRepository.java
	@Query("SELECT m, p FROM Member m LEFT OUTER JOIN Profile p " + 
	"ON m.uid=p.member WHERE m.uid=?1 AND p.current=true")
	public List<Object[]> getMemberWithProfile(String uid);
```

# �ڷ�ǰ� ÷�� ������ ���� (�ܹ��� OneToMany, Join �̿�)
![123](https://user-images.githubusercontent.com/20277647/52335378-9b697300-2a45-11e9-9e58-49c0df594392.PNG)
- �ڷḦ ��Ÿ���� PDSBoard ��ƼƼ Ŭ������, ÷�� ������ ��Ÿ���� PDSFile ��ƼƼ Ŭ������ �ۼ�
- OneToMany ������ ���� Board Ŭ�������� File Ŭ������ ��ü ����
- ���⼭ ������ ���� �ܹ��� �̱� ������ ���� Ŭ���������� �����Ѵ�.
- ���� OneToMany�� ��� JoinTable�̳� JoinColumn ������̼��� �Բ� �����ؾ� �Ѵ�.
- �׸��� 2���� ���̺� �ѹ��� ������ ������ ���� Cascade ������ �̿��Ѵ�.
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
- �ڷ�� ÷�� ������ ���� �ڷ� ��ȣ�� �������� ����ϴ� ��
```
	@Query("SELECT p, count(f) FROM PDSBoard p LEFT OUTER JOIN p.files f " + 
			"WHERE p.pid>0 GROUP BY p ORDER BY p.pid DESC")
	public List<Object[]> getSummary();
```

# JPQL�� �̿��Ͽ� DML ó��
- Query ������̼��� �⺻������ select ������ ����������, Modifying ������̼��� �̿��ϸ� DML �۾� ó�� ����
- UPDATE ��
```
	@Modifying
	@Query("UPDATE FROM PDSFile f set f.pdsfile = ?2 WHERE f.fno = ?1")
	public int updatePDSFile(Long fno, String newFileName);
```
- DELETE ��
```
	@Modifying
	@Query("DELETE FROM PDSFile f where f.fno=?1")
	public int deletePDSFile(Long fno);
```

# �Խù��� ����� ���� (����� ����)
![123](https://user-images.githubusercontent.com/20277647/52397879-6c610900-2afa-11e9-8dfc-00e94ab5e66e.PNG)
- �Խù��� ��Ÿ���� FreeBoard ��ƼƼ Ŭ������, ����� ��Ÿ���� FreeBoardReply ��ƼƼ Ŭ������ �ۼ�
- �Խù� ���忡���� ����� �ϴ�� �����̹Ƿ� OneToMany�� ����
- ��� ���忡���� �Խù��� �ٴ��� �����̹Ƿ� ManyToOne���� ����
- ����� �̱� ������ ������� PK�� �ǰ�, ������� FK�� �Ǵ����� ����� �־�� �Ѵ�.
- PK ���� mappedBy��� �Ӽ��� �̿��ؼ� �ڽ��� �ٸ� ��ü���� �ſ��ִٴ� ���� ����Ѵ�. (OneToMany ��)
  - ManyToOne�� DB ���̺��� ���캸�� Į���� �߰� �ȴ�.
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
- �� ��ƼƼ Ŭ������ �´� Repository �������̽� �ۼ�
- Repository�� �̿��ؼ� ������ ������ ����
  - ManyToOne�� DB ���̺��� ���캸�� (FK)Į���� �߰� �Ǿ��� ������ �̺κе� set���� ���� (bno)
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
- Join�� Paging ��� ����
```
public interface FreeBoardRepository extends CrudRepository<FreeBoard, Long> {

	@Query("SELECT b.bno, b.title, count(r) FROM FreeBoard b "+
			"LEFT OUTER JOIN b.replies r "+
			"WHERE b.bno > 0 GROUP BY b")
	public List<Object[]> getPage(Pageable page);
}

// ���
	@Test
	public void testList3() {
		Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "bno");
		
		boardRepo.getPage(page).forEach(arr -> System.out.println(Arrays.toString(arr)));
	}
```

# JPA���� �ε��� ó��
- �ε��� ���� : https://hashcode.co.kr/questions/1551/%EC%99%9C-db-%ED%85%8C%EC%9D%B4%EB%B8%94%EC%9D%98-%EB%AA%A8%EB%93%A0-%EC%BB%AC%EB%9F%BC%EC%97%90-%EC%9D%B8%EB%8D%B1%EC%8A%A4%EB%A5%BC-%EA%B1%B8%EB%A9%B4-%EC%95%88%EB%90%98%EB%82%98%EC%9A%94
- ���̺� ���� �� �ε����� ����ǵ��� ���� (��ƼƼ Ŭ����)
```
@Entity
@Table(name="tbl_free_replies", indexes = {@Index(unique=false, columnList="board_bno")} )
```