# REST 방식
- URL이 콘텐츠 자체를 의미하며 '명사'가 URL의 구분이 되고, 전송 방식이 동사의 역할을 한다.
- 각 단계는 '/'를 이용해서 구분 처리하고, 마지막에는 가장 상세한 정보를 사용한다.
- GET : 특정 리소스를 조회(read)하는 용도로 사용
  - ex) /products/123
- POST: 특정 리소스를 생성(create)하는 용도로 사용
  - ex) /products/
- PUT : 특정 리소스를 수정
- DELETE : 특정 리소스를 삭제

# REST 방식 처리를 위한 URL 설계
- 특정 게시물의 댓글 추가 : POST : /replies/게시물 번호
- 특정 게시물의 댓글 삭제 : DELETE : /replies/게시물 번호/댓글 번호
- 특정 게시물의 댓글 수정 : PUT : /replies/게시물 번호 (어떤 댓글을 수정할 지 rno는 body에 보냄)
- 특정 게시물의 모든 댓글 : GET : /replies/게시물 번호
```
	@Transactional
	@PostMapping("/{bno}")

	@Transactional
	@DeleteMapping("/{bno}/{rno}")

	@Transactional
	@PutMapping("/{bno}")

	@GetMapping("/{bno}")
```


# REST 처리를 위한 Controller 작성
- RestController 어노테이션 사용
```
@RestController
@RequestMapping("/replies/*")
public class WebReplyController {

	@Autowired
	private WebReplyRepository replyRepo;
	
	
}
```

# REST 방식에서 데이터 처리
- Ajax를 이용하기 위해 JSON 형태의 데이터를 주고 받도록 한다.
  - 양방향으로 설계한 경우 무한히 반복해서 생성되는 것을 방지하기 위해 ManyToOne쪽에 JsonIgnore을 사용
```
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	private WebBoard board;
```
- PathVariable 어노테이션 : URI의 일부를 파라미터로 받기 위해서 사용
- RequestBody 어노테이션 : JSON으로 전달되는 데이터를 객체로 자동으로 변환하도록 처리하는 역할
- ResponseEntity 타입 : 직접 Http Response의 상태 코드와 데이터를 직접 제어해서 처리할 수 있는 장점
- HttpStatus.CREATED : HTTP 상태 코드 중 201을 의미하는 created 메시지를 전송
```
	@PostMapping("/{bno}")
	public ResponseEntity<Void> addReply(
			@PathVariable("bno") Long bno, @RequestBody WebReply reply){
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
```
- POST로 요청을 보내며, JSON 형태의 데이터를 body에 실어서 보냄 (Postman 등으로 테스트)
```
// http://localhost:8080/replies/300
{	
	"replyText": "샘플 댓글",
	"replyer": "user00"
}
```
- 댓글 등록 후 댓글 리스트를 갱신
  - 댓글 등록, 삭제, 수정 후 getListByBoard 메소드를 통해 해당 게시글의 댓글 정보를 새로 return
```
// WebReplyRepository.java
public interface WebReplyRepository extends CrudRepository<WebReply, Long> {

	@Query("SELECT r FROM WebReply r WHERE r.board = ?1 " + 
			"AND r.rno > 0 ORDER BY r.rno ASC")
	public List<WebReply> getRepliesOfBoard(WebBoard board);
}

// WebReplyController.java
@RestController
@RequestMapping("/replies/*")
public class WebReplyController {

	@Autowired
	private WebReplyRepository replyRepo;
	
	@Transactional
	@PostMapping("/{bno}")
	public ResponseEntity<List<WebReply>> addReply(
			@PathVariable("bno") Long bno, @RequestBody WebReply reply){
		
		WebBoard board = new WebBoard();
		board.setBno(bno);
		reply.setBoard(board);
		replyRepo.save(reply);
		return new ResponseEntity<>(getListByBoard(board), HttpStatus.CREATED);
	}
	
	private List<WebReply> getListByBoard(WebBoard board) throws RuntimeException{
		return replyRepo.getRepliesOfBoard(board);
	}
}
```
- 댓글 처리 후 조회 화면을 Ajax로 처리하기 위해 Javascript 작성
  - static/js/reply.js (모듈 패턴 이용)
```
var replyManager = (function(){
	
	var getAll = function(obj, callback){
		console.log("get All...");
		$.getJSON('/replies/' + obj, callback);
	}
	
	var add = function(obj, callback){
		console.log("add...");
		
		$.ajax({
			type : 'post',
			url : '/replies/' + obj.bno,
			data : JSON.stringify(obj),
			dataType : 'json',
			contentType : "application/json",
			success : callback
		});
	}

	...

	return {
		getAll: getAll,
		add: add,
		update: update,
		remove: remove
	}
})();
```
- view 에서 사용
```
...
replyManager.getAll([[${vo.bno}]], printList);
...
```

# Querydsl을 이용한 동적으로 JPQL 처리하기
- 사용자 정의 인터페이스 설계
  - 페이징 처리와 검색 조건에 따라서 JPQL이 달라지도록 하는 것이 목적
```
public interface CustomWebBoard {

	public Page<Object[]> getCustomPage(String type, String keyword, Pageable page);
}
```
- 엔티티의 Repository 인터페이스 설계
```
public interface CustomCrudRepository extends CrudRepository<WebBoard, Long>, CustomWebBoard{

}
```
- 사용자 정의 인터페이스 구현
  - 실제 JPQL을 코드로 처리하는 작업은 '엔티티 Repository 이름' + 'Impl'로 작성한다.
  - QuerydslRepositorySupport를 상속 받고, 생성자를 구현 해야 한다.
  - Querydsl과 Qdomain을 이용한다. (Querydsl에 대한 셋팅이 미리 되어 있어야 한다.)
```
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;

public class CustomCrudRepositoryImpl extends QuerydslRepositorySupport implements CustomWebBoard {

	public CustomCrudRepositoryImpl() {
		super(WebBoard.class);
	}
	
	public Page<Object[]> getCustomPage(String type, String keyword, Pageable page){
		
		QWebBoard b = QWebBoard.webBoard;
		QWebReply r = QWebReply.webReply;
		
		JPQLQuery<WebBoard> query = from(b);
		
		JPQLQuery<Tuple> tuple = query.select(b.bno, b.title, r.count(), b.writer, b.regdate);
		
		tuple.leftJoin(r);
		tuple.on(b.bno.eq(r.board.bno));
		tuple.where(b.bno.gt(0L));
		
		if(type != null) {
			switch(type.toLowerCase()) {
			case "t":
				tuple.where(b.title.like("%" + keyword + "%"));
				break;
			case "c":
				tuple.where(b.content.like("%" + keyword + "%"));
				break;
			case "w":
				tuple.where(b.writer.like("%" + keyword + "%"));
				break;
			}
		}
		
		tuple.groupBy(b.bno);		
		tuple.orderBy(b.bno.desc());
		
		tuple.offset(page.getOffset());
		tuple.limit(page.getPageSize());
		
		List<Tuple> list = tuple.fetch();
		
		List<Object[]> resultList = new ArrayList<>();
		
		list.forEach(t -> {
			resultList.add(t.toArray());
		});
		
		long total = tuple.fetchCount();
		
		return new PageImpl<>(resultList, page, total);
	}
}
```
- 구현한 인터페이스 사용
```
	@Autowired
	CustomCrudRepository repo;
	
	@Test
	public void test1() {
		Pageable pageable = PageRequest.of(0, 10, Direction.DESC, "bno");
		
		String type="w";
		String keyword="user09";
		
		Page<Object[]> result = repo.getCustomPage(type, keyword, pageable);
		
		result.getContent().forEach(arr -> {
			System.out.println(Arrays.toString(arr));
		});
		
	}
```
