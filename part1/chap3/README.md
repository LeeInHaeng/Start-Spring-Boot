# 쿼리 메소드
- 조금 복잡한 쿼리를 작성하기 위해 사용 (select 관련 쿼리만)
- 메소드의 이름만으로 필요한 쿼리를 만들어 내는 기능으로, 네이밍 룰만 알고 있어도 사용할 수 있다.
- 사용 Dependency
  - DevTools, JPA, MySQL, Web
- application.properties 파일 작성 (hibernate, mysql 연동)
- Entity 클래스 및 Repository 인터페이스 작성
- 쿼리 메소드 이용
  - find클래스By칼럼명, read클래스By칼럼명, query...By..., get...By..., count...By...
  - 쿼리 메소드의 리턴타입 : Page<T>, Slice<T>, List<T>
  - Repository 인터페이스에 메소드 작성 후 이용
```
// BoardRepository.java
public interface BoardRepository extends CrudRepository<Board, Long> {

	public List<Board> findBoardByTitle(String title);
}
// 사용
	@Test
	public void testByTitle() {
		repo.findBoardByTitle("제목...177")
		.forEach(board -> System.out.println(board));
	}
```
- https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
- https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#_supported_query_keywords

# 쿼리 메소드 키워드 예제
- findBy 키워드 : SQL 문에서 특정한 칼럼의 값을 조회할 때 사용
```
public Collection<Board> findByWriter(String writer);
```
- like 키워드 사용
  - 단순 like : Like
  - 키워드 + '%' : StartingWith
  - '%' + 키워드 : EndingWith
  - '%' + 키워드 + '%' : Containing
```
public Collection<Board> findByWriterContaining(String writer);
```
- and 혹은 or 조건 처리 : And와 Or 키워드 사용
```
public Collection<Board> findByTitleContainingOrContentContaining(String title, String content);
```
- 부등호 처리 : GreaterThan, LessThan 키워드 사용
```
// title like % ? % and bno > ?
public Collection<Board> findByTitleContainingAndBnoGreaterThan(String keyword, Long num);
```
- order by 처리 : 'OrderBy' + 칼럼 + 'Asc or Desc'
```
// bno > ? order by bno desc
public Collection<Board> findByBnoGreaterThanOrderByBnoDesc(Long bno);
```
- 페이징 처리 : SQL에서 limit 키워드에 해당
  - ex) 전체 게시물 개수 중 한 페이지에 몇 개의 게시글을 보여 줄지 결정하는데 사용
  - springframework.data.domain.Page 타입 사용
```
// bno > ? order by bno desc limit ?, ?
public Page<Board> FindByBnoGreaterThanOrderByBnoDesc(Long bno, Pageable paging);

	@Test
	public void testBnoPaging() {
		repo.findByBnoGreaterThanOrderByBnoDesc(0L, PageRequest.of(0, 10))
		.forEach(board -> System.out.println(board));
	}
```

# Query 어노테이션을 통한 JPQL 사용
- 필요한 몇 개의 컬럼만 추출할 수 있다.
- 튜닝된 복잡한 SQL문을 사용할 수 있다.
- Repository에 지정된 엔티티 타입 뿐 아니라 필요한 엔티티 타입을 다양하게 사용할 수 있다.
- natieQuery 속성을 true로 함으로써 데이터베이스에 사용하는 SQL을 그대로 사용할 수 있다.
```
// Repository Interface

@Query("SELECT b.bno, b.title, b.content " + 
	"FROM Board b WHERE b.content LIKE %:content% AND b.bno>0 ORDER BY b.bno DESC")
public Page<Object[]> findByContentPage(@Param("content") String content, Pageable paging);
```
- 전체 컬럼을 추출하는 경우(select b)에는 ```Page<Board>```, 필요한 컬럼만 추출하는 경우에는 ```Page<Object[]>```