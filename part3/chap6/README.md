# MVC, JPA, Thymeleaf�� �̿��� �Խ��� ����
- �ʿ� ���̺귯�� : JPA, DevTools, Web, Thymeleaf, MySQL
- JPA ����� ���� application.properties �ۼ�
- Thymeleaf layout dialect ����� ���� ���̺귯�� �ٿ� (pom.xml)
```
<!-- https://mvnrepository.com/artifact/nz.net.ultraq.thymeleaf/thymeleaf-layout-dialect -->
<dependency>
    <groupId>nz.net.ultraq.thymeleaf</groupId>
    <artifactId>thymeleaf-layout-dialect</artifactId>
    <version>2.3.0</version>
</dependency>
```
- Thymeleaf layout dialect ����� ���� templates ������ layout ���� ���� �� layout.html�� static�� �ʿ��� �ڿ��� ����
- ��Ʈ��Ʈ�� ����� ���� layout.html�� CDN �߰� �� layout.html ������
  - http://bootstrapk.com/getting-started/#download
```
<!-- �������� �ּ�ȭ�� �ֽ� CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

<!-- �ΰ����� �׸� -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

<!-- �������� �ּ�ȭ�� �ֽ� �ڹٽ�ũ��Ʈ -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

  <!-- content body -->
  <div class="page-header">
  	<h1>
  		Boot06 Project <small>for Spring MVC + JPA</small>
  	</h1>
  </div>
  <div class="panel panel-default" layout:fragment="content"></div>
```
- ��Ʈ�ѷ� �ۼ�
- ��Ʈ�ѷ��� URI�� �°� templates ���� �ȿ� ��ο� �´� ������ html ����
- Thymeleaf�� layout�� ����Ͽ� html ���� �ۼ�
  - layout.html�� �ۼ��� fragment ���
```
	<div layout:fragment="content">
		<div class="panel-heading">List Page</div>
		<div class="panel-body">
			list content......
		</div>
	</div>	
/*
���� html ������ �Ʒ��� ���� �����
<div class="panel panel-default" layout:fragment="content">
	<div class="panel-heading">List Page</div>
	<div class="panel-body">
		list content......
	</div>
</div>
*/
```
- ���� ������ ����ϱ� ���� Querydsl ����
```
// pom.xml
		<!-- https://mvnrepository.com/artifact/com.querydsl/querydsl-apt -->
		<dependency>
		    <groupId>com.querydsl</groupId>
		    <artifactId>querydsl-apt</artifactId>
		    <version>4.2.1</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/com.querydsl/querydsl-jpa -->
		<dependency>
		    <groupId>com.querydsl</groupId>
		    <artifactId>querydsl-jpa</artifactId>
		    <version>4.2.1</version>
		</dependency>


			<plugin>
			    <groupId>com.mysema.maven</groupId>
			    <artifactId>apt-maven-plugin</artifactId>
			    <version>1.1.3</version>
			    <executions>
			        <execution>
			            <goals>
			                <goal>process</goal>
			            </goals>
			            <configuration>
			                <outputDirectory>target/generated-sources/java</outputDirectory>
			                <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
			            </configuration>
			        </execution>
			        </executions>
			    </plugin>
```
- target/generated-sources/java�� Ŭ������ �������� �ʴ� ���
  - ������Ʈ Properties ---> Java Build Path ---> JRE System Library ---> Edit ---> Installed JRE ---> JDK ���� ---> Apply
  - Run as ---> Run Configurations ---> Maven Build ---> New ---> Name : Code Gen / Base Directory : Workspace�� ���� ���� ������Ʈ ���� / Goals : eclipse:eclipse ---> Run
- ��ƼƼ Ŭ������ Repository ����
- �׽�Ʈ �ڵ带 �̿��Ͽ� Dummy ������ �߰�

# MVC, JPA, Thymeleaf�� �̿��� �Խ��� ���� - Querydsl ���
- Repository���� Querydsl�� ����ϵ��� ���� �� ���ǿ� ���� SQL ó��
```
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface WebBoardRepository extends CrudRepository<WebBoard, Long>, QuerydslPredicateExecutor<WebBoard>{

	public default Predicate makePredicate(String type, String keyword) {
		BooleanBuilder builder = new BooleanBuilder();
		
		QWebBoard board = QWebBoard.webBoard;
		
		// bno > 0
		builder.and(board.bno.gt(0));
		
		// type if ~ else
		if(type==null) {
			return builder;
		}
		
		switch(type) {
		case "t":
			builder.and(board.title.like("%" + keyword + "%"));
			break;
		case "c":
			builder.and(board.content.like("%" + keyword + "%"));
			break;
		case "w":
			builder.and(board.writer.like("%" + keyword + "%"));
			break;
		}
		
		return builder;
	}
}
```
- Querydsl�� ���� ������ �޼ҵ� ���
```
	@Test
	public void testList2() {
		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "bno");
		
		Page<WebBoard> result = repo.findAll(repo.makePredicate("t", "10"),pageable);
		
		System.out.println("PAGE : " + result.getPageable());
		
		result.getContent().forEach(board -> System.out.println(board));
	}
```

# MVC, JPA, Thymeleaf�� �̿��� �Խ��� ���� - Post ó��
```
	@PostMapping("/register")
	public String registerPost(@ModelAttribute("vo") WebBoard vo, RedirectAttributes rttr) {
		repo.save(vo);
		rttr.addFlashAttribute("msg","success");
		// addAttribute�� URL�� �߰��Ǿ ���۵ȴ�.
		
		return "redirect:/boards/list";
	}
```
- Post-Redirect-Get ���
- RedirectAttributes�� URL�� ������ �ʴ� ���ڿ��� ������ �ֱ� ������ �������� �ּ�â�� ������ ����.
- Ŭ���� ������ Thymeleaf html�� �Ѱ��ֱ� ���� ModelAttribute ������̼��� ���
- RedirectAttributes�� ���� ���޵� ������ callback ó�� ����
```
var msg = [[${msg}]];
  			
if(msg=='success'){
	alert("���������� ó���Ǿ����ϴ�.");
}
```
- form�±׿� hidden ���� ������ ���� ��, �� Ŭ���� �°� ���ݾ� ������ �־ submit ����
```
<form id='f1' th:action="@{list}" method="get">
	<input type='hidden' name='page' th:value=${result.currentPageNum}>
	<input type='hidden' name='size' th:value=${result.currentPage.pageSize}>
	<input type='hidden' name='type' th:value=${pageVO.type}>
	<input type='hidden' name='keyword' th:value=${pageVO.keyword}>
</form>

var formObj = $("#f1");

$(".pagination a").click(e => {
	e.preventDefault();

	formObj.find('[name="page"]').val(e.target.innerText);
	formObj.submit();
});

// formObj.attr("action","list");
// formObj.attr("method","get");
```
