# REST ���
- URL�� ������ ��ü�� �ǹ��ϸ� '���'�� URL�� ������ �ǰ�, ���� ����� ������ ������ �Ѵ�.
- �� �ܰ�� '/'�� �̿��ؼ� ���� ó���ϰ�, ���������� ���� ���� ������ ����Ѵ�.
- GET : Ư�� ���ҽ��� ��ȸ(read)�ϴ� �뵵�� ���
  - ex) /products/123
- POST: Ư�� ���ҽ��� ����(create)�ϴ� �뵵�� ���
  - ex) /products/
- PUT : Ư�� ���ҽ��� ����
- DELETE : Ư�� ���ҽ��� ����

# REST ��� ó���� ���� URL ����
- Ư�� �Խù��� ��� �߰� : POST : /replies/�Խù� ��ȣ
- Ư�� �Խù��� ��� ���� : DELETE : /replies/�Խù� ��ȣ/��� ��ȣ
- Ư�� �Խù��� ��� ���� : PUT : /replies/�Խù� ��ȣ (� ����� ������ �� rno�� body�� ����)
- Ư�� �Խù��� ��� ��� : GET : /replies/�Խù� ��ȣ
```
	@Transactional
	@PostMapping("/{bno}")

	@Transactional
	@DeleteMapping("/{bno}/{rno}")

	@Transactional
	@PutMapping("/{bno}")

	@GetMapping("/{bno}")
```


# REST ó���� ���� Controller �ۼ�
- RestController ������̼� ���
```
@RestController
@RequestMapping("/replies/*")
public class WebReplyController {

	@Autowired
	private WebReplyRepository replyRepo;
	
	
}
```

# REST ��Ŀ��� ������ ó��
- Ajax�� �̿��ϱ� ���� JSON ������ �����͸� �ְ� �޵��� �Ѵ�.
  - ��������� ������ ��� ������ �ݺ��ؼ� �����Ǵ� ���� �����ϱ� ���� ManyToOne�ʿ� JsonIgnore�� ���
```
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	private WebBoard board;
```
- PathVariable ������̼� : URI�� �Ϻθ� �Ķ���ͷ� �ޱ� ���ؼ� ���
- RequestBody ������̼� : JSON���� ���޵Ǵ� �����͸� ��ü�� �ڵ����� ��ȯ�ϵ��� ó���ϴ� ����
- ResponseEntity Ÿ�� : ���� Http Response�� ���� �ڵ�� �����͸� ���� �����ؼ� ó���� �� �ִ� ����
- HttpStatus.CREATED : HTTP ���� �ڵ� �� 201�� �ǹ��ϴ� created �޽����� ����
```
	@PostMapping("/{bno}")
	public ResponseEntity<Void> addReply(
			@PathVariable("bno") Long bno, @RequestBody WebReply reply){
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
```
- POST�� ��û�� ������, JSON ������ �����͸� body�� �Ǿ ���� (Postman ������ �׽�Ʈ)
```
// http://localhost:8080/replies/300
{	
	"replyText": "���� ���",
	"replyer": "user00"
}
```
- ��� ��� �� ��� ����Ʈ�� ����
  - ��� ���, ����, ���� �� getListByBoard �޼ҵ带 ���� �ش� �Խñ��� ��� ������ ���� return
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
- ��� ó�� �� ��ȸ ȭ���� Ajax�� ó���ϱ� ���� Javascript �ۼ�
  - static/js/reply.js (��� ���� �̿�)
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
- view ���� ���
```
...
replyManager.getAll([[${vo.bno}]], printList);
...
```

# Querydsl�� �̿��� �������� JPQL ó���ϱ�
- ����� ���� �������̽� ����
  - ����¡ ó���� �˻� ���ǿ� ���� JPQL�� �޶������� �ϴ� ���� ����
```
public interface CustomWebBoard {

	public Page<Object[]> getCustomPage(String type, String keyword, Pageable page);
}
```
- ��ƼƼ�� Repository �������̽� ����
```
public interface CustomCrudRepository extends CrudRepository<WebBoard, Long>, CustomWebBoard{

}
```
- ����� ���� �������̽� ����
  - ���� JPQL�� �ڵ�� ó���ϴ� �۾��� '��ƼƼ Repository �̸�' + 'Impl'�� �ۼ��Ѵ�.
  - QuerydslRepositorySupport�� ��� �ް�, �����ڸ� ���� �ؾ� �Ѵ�.
  - Querydsl�� Qdomain�� �̿��Ѵ�. (Querydsl�� ���� ������ �̸� �Ǿ� �־�� �Ѵ�.)
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
- ������ �������̽� ���
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
