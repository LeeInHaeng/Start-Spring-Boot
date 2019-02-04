# ���� �޼ҵ�
- ���� ������ ������ �ۼ��ϱ� ���� ��� (select ���� ������)
- �޼ҵ��� �̸������� �ʿ��� ������ ����� ���� �������, ���̹� �길 �˰� �־ ����� �� �ִ�.
- ��� Dependency
  - DevTools, JPA, MySQL, Web
- application.properties ���� �ۼ� (hibernate, mysql ����)
- Entity Ŭ���� �� Repository �������̽� �ۼ�
- ���� �޼ҵ� �̿�
  - findŬ����ByĮ����, readŬ����ByĮ����, query...By..., get...By..., count...By...
  - ���� �޼ҵ��� ����Ÿ�� : Page<T>, Slice<T>, List<T>
  - Repository �������̽��� �޼ҵ� �ۼ� �� �̿�
```
// BoardRepository.java
public interface BoardRepository extends CrudRepository<Board, Long> {

	public List<Board> findBoardByTitle(String title);
}
// ���
	@Test
	public void testByTitle() {
		repo.findBoardByTitle("����...177")
		.forEach(board -> System.out.println(board));
	}
```
- https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
- https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#_supported_query_keywords

# ���� �޼ҵ� Ű���� ����
- findBy Ű���� : SQL ������ Ư���� Į���� ���� ��ȸ�� �� ���
```
public Collection<Board> findByWriter(String writer);
```
- like Ű���� ���
  - �ܼ� like : Like
  - Ű���� + '%' : StartingWith
  - '%' + Ű���� : EndingWith
  - '%' + Ű���� + '%' : Containing
```
public Collection<Board> findByWriterContaining(String writer);
```
- and Ȥ�� or ���� ó�� : And�� Or Ű���� ���
```
public Collection<Board> findByTitleContainingOrContentContaining(String title, String content);
```
- �ε�ȣ ó�� : GreaterThan, LessThan Ű���� ���
```
// title like % ? % and bno > ?
public Collection<Board> findByTitleContainingAndBnoGreaterThan(String keyword, Long num);
```
- order by ó�� : 'OrderBy' + Į�� + 'Asc or Desc'
```
// bno > ? order by bno desc
public Collection<Board> findByBnoGreaterThanOrderByBnoDesc(Long bno);
```
- ����¡ ó�� : SQL���� limit Ű���忡 �ش�
  - ex) ��ü �Խù� ���� �� �� �������� �� ���� �Խñ��� ���� ���� �����ϴµ� ���
  - springframework.data.domain.Page Ÿ�� ���
```
// bno > ? order by bno desc limit ?, ?
public Page<Board> FindByBnoGreaterThanOrderByBnoDesc(Long bno, Pageable paging);

	@Test
	public void testBnoPaging() {
		repo.findByBnoGreaterThanOrderByBnoDesc(0L, PageRequest.of(0, 10))
		.forEach(board -> System.out.println(board));
	}
```

# Query ������̼��� ���� JPQL ���
- �ʿ��� �� ���� �÷��� ������ �� �ִ�.
- Ʃ�׵� ������ SQL���� ����� �� �ִ�.
- Repository�� ������ ��ƼƼ Ÿ�� �� �ƴ϶� �ʿ��� ��ƼƼ Ÿ���� �پ��ϰ� ����� �� �ִ�.
- natieQuery �Ӽ��� true�� �����ν� �����ͺ��̽��� ����ϴ� SQL�� �״�� ����� �� �ִ�.
```
// Repository Interface

@Query("SELECT b.bno, b.title, b.content " + 
	"FROM Board b WHERE b.content LIKE %:content% AND b.bno>0 ORDER BY b.bno DESC")
public Page<Object[]> findByContentPage(@Param("content") String content, Pageable paging);
```
- ��ü �÷��� �����ϴ� ���(select b)���� ```Page<Board>```, �ʿ��� �÷��� �����ϴ� ��쿡�� ```Page<Object[]>```