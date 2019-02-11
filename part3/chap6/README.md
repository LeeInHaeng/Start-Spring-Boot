# MVC, JPA, Thymeleaf를 이용한 게시판 예제
- 필요 라이브러리 : JPA, DevTools, Web, Thymeleaf, MySQL
- JPA 사용을 위해 application.properties 작성
- Thymeleaf layout dialect 사용을 위해 라이브러리 다운 (pom.xml)
```
<!-- https://mvnrepository.com/artifact/nz.net.ultraq.thymeleaf/thymeleaf-layout-dialect -->
<dependency>
    <groupId>nz.net.ultraq.thymeleaf</groupId>
    <artifactId>thymeleaf-layout-dialect</artifactId>
    <version>2.3.0</version>
</dependency>
```
- Thymeleaf layout dialect 사용을 위해 templates 폴더에 layout 폴더 생성 후 layout.html과 static에 필요한 자원들 복사
- 부트스트랩 사용을 위해 layout.html에 CDN 추가 및 layout.html 디자인
  - http://bootstrapk.com/getting-started/#download
```
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

<!-- 부가적인 테마 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

  <!-- content body -->
  <div class="page-header">
  	<h1>
  		Boot06 Project <small>for Spring MVC + JPA</small>
  	</h1>
  </div>
  <div class="panel panel-default" layout:fragment="content"></div>
```
- 컨트롤러 작성
- 컨트롤러의 URI에 맞게 templates 폴더 안에 경로에 맞는 폴더와 html 생성
- Thymeleaf와 layout을 사용하여 html 파일 작성
  - layout.html에 작성된 fragment 사용
```
	<div layout:fragment="content">
		<div class="panel-heading">List Page</div>
		<div class="panel-body">
			list content......
		</div>
	</div>	
/*
실제 html 구조는 아래와 같이 적용됨
<div class="panel panel-default" layout:fragment="content">
	<div class="panel-heading">List Page</div>
	<div class="panel-body">
		list content......
	</div>
</div>
*/
```
- 동적 쿼리를 사용하기 위해 Querydsl 설정
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
- target/generated-sources/java에 클래스가 생성되지 않는 경우
  - 프로젝트 Properties ---> Java Build Path ---> JRE System Library ---> Edit ---> Installed JRE ---> JDK 선택 ---> Apply
  - Run as ---> Run Configurations ---> Maven Build ---> New ---> Name : Code Gen / Base Directory : Workspace를 통해 현재 프로젝트 선택 / Goals : eclipse:eclipse ---> Run
- 엔티티 클래스와 Repository 설계
- 테스트 코드를 이용하여 Dummy 데이터 추가

# MVC, JPA, Thymeleaf를 이용한 게시판 예제 - Querydsl 사용
- Repository에서 Querydsl을 사용하도록 설정 및 조건에 따른 SQL 처리
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
- Querydsl을 위해 구현한 메소드 사용
```
	@Test
	public void testList2() {
		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "bno");
		
		Page<WebBoard> result = repo.findAll(repo.makePredicate("t", "10"),pageable);
		
		System.out.println("PAGE : " + result.getPageable());
		
		result.getContent().forEach(board -> System.out.println(board));
	}
```

# MVC, JPA, Thymeleaf를 이용한 게시판 예제 - Post 처리
```
	@PostMapping("/register")
	public String registerPost(@ModelAttribute("vo") WebBoard vo, RedirectAttributes rttr) {
		repo.save(vo);
		rttr.addFlashAttribute("msg","success");
		// addAttribute는 URL에 추가되어서 전송된다.
		
		return "redirect:/boards/list";
	}
```
- Post-Redirect-Get 방식
- RedirectAttributes는 URL로 보이지 않는 문자열을 생성해 주기 때문에 브라우저의 주소창에 보이지 않음.
- 클래스 정보를 Thymeleaf html에 넘겨주기 위해 ModelAttribute 어노테이션을 사용
- RedirectAttributes를 통해 전달된 변수로 callback 처리 가능
```
var msg = [[${msg}]];
  			
if(msg=='success'){
	alert("정상적으로 처리되었습니다.");
}
```
- form태그에 hidden 값들 셋팅해 놓은 후, 각 클릭에 맞게 조금씩 변경해 주어서 submit 수행
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
