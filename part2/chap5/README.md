# Thymeleaf (springboot 템플릿) 사용
- 사용 Dependency
  - DevTools, Thymeleaf, Web
  - src/main/resources에서 static은 js, css, html, 이미지 파일 / templates는 Thymeleaf를 이용한 템플릿들
- Window ---> Preferences ---> General ---> Workspace에서 utf-8로 인코딩 변경
- 개발 시 캐싱되는 불편함을 제거하기 위해 캐시를 false로 설정
```
// application.properties
spring.thymeleaf.cache=false
```
- 템플릿을 이용하기 위한 컨트롤러 작성
```
@Controller
public class SampleController {

	@GetMapping("/sample1")
	public void sample1(Model model) {
		model.addAttribute("greeting", "Hello World");
	}
}
```
- Thymeleaf 사용 편의를 위해 관련 플러그인 설치
  - https://github.com/thymeleaf/thymeleaf-extras-eclipse-plugin 에서 Installation 부분 참고
  - eclipse에서 help ---> install new softwares 메뉴에서 github에 나와있는 URL 추가
  - Name : Thymeleaf / Location : http://www.thymeleaf.org/eclipse-plugin-update-site/
- src/main/resources/templates에서 html 파일 작성
  - 가장 먼저 Thymeleaf를 사용한다는 것을 명시해 주고, html을 작성한다.
```
<html xmlns:th="http://www.thymeleaf.org">

<head>
<title>Thymeleaf3</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
	<h1 th:text="${greeting}"></h1>
</body>

</html>
```

# Thymeleaf (springboot 템플릿) 기본 문법 예제
- th:text = 태그의 내용물로 문자열을 출력
```
<h1 th:text="${vo}"></h1>
```
- th:utext = 문자열이 아닌 HTML 자체를 출력하는데 사용
- th:each = 리스트, Iterable, Map, 배열 등을 사용할 수 있다.
```
	<table border="1">
		<tr>
			<td>MID</td>
			<td>MNAME</td>
			<td>REGDATE</td>
		</tr>
		
		<tr th:each="member : ${list}">
			<td th:text="${member.mid}"></td>
			<td th:text="${member.mname}"></td>
			<td th:text="${#dates.format(member.regdate, 'yyyy-MM-dd')}"></td>
		</tr>
	</table>
```
- th:each = iterable을 이용해 여러가지 상태 확인 가능
  - index : 0부터 시작하는 인덱스 번호
  - count : 1부터 시작하는 번호
  - size : 현재 대상의 length 혹은 size
  - odd/even : 현재 번호의 홀수/짝수 여부 (true, false)
  - first/last : 처음 요소인지 마지막 요소인지 판단 (true, false)
```
	<table border="1">
		<tr>
			<td>INDEX</td>
			<td>SIZE</td>
			<td>ODD/EVEN</td>
			<td>MID</td>
			<td>MNAME</td>
			<td>REGDATE</td>
		</tr>
		
		<tr th:each="member, iterState : ${list}">
			<td th:text="${iterState.index}"></td>
			<td th:text="${iterState.size}"></td>
			<td th:text="${iterState.odd + ' ' + iterState.even}"></td>
			<td th:text="${member.mid}"></td>
			<td th:text="${member.mname}"></td>
			<td th:text="${#dates.format(member.regdate, 'yyyy-MM-dd')}"></td>
		</tr>
	</table>
```
- th:with = 특정한 범위에서만 유효한 지역변수를 선언
```
<table border="1" th:with="target='u0001'">
```
- th:if ~ th:unless = if ~ else 처리를 위해 사용
```
<tr th:each="member, iterState : ${list}">
	<td th:if="${member.mid}">
		<a href="/modify" th:if="${member.mid == target}">MODIFY</a>
		<p th:unless="${member.mid == target}">VIEW</p>
	</td>
</tr>
```
- th:inline = javascript를 사용하기 위해 사용
  - model에서 넘어온 변수를 "${변수}" 로 접근하는 것이 아닌 "[[${변수}]]" 로 접근한다.
```
	<script th:inline="javascript">
		var result = [[${result}]];
		console.log(result);
	</script>
```
- th:fragment = 일부분만 템플릿 교체를 위해 사용
```
<div th:fragment="footer">
	Footer 파일입니다.
</div>
```
- th:insert = fragment를 인클루드 시키기 위해 사용 {경로::fragment}
```
	<div th:insert="~{fragments/header::header}"></div>
	<div th:insert="~{fragments/footer::footer}"></div>
```

# Thymeleaf 유틸리티 객체 사용 예제
- 날짜 관련 : ```#dates```
```
	<h2 th:text="${#dates.format(now, 'yyyy-MM-dd')}"></h2>
	<div th:with="timeValue=${#dates.createToday()}">
		<p>[[${timeValue}]]</p>
	</div>
```
- 숫자 관련 : ```#numbers```
```
	<h2 th:text="${#numbers.formatInteger(price,3,'COMMA')}"></h2>
	<div th:with="priceValue=99.87654">
		<p th:text="${#numbers.formatInteger(priceValue,3,'COMMA')}"></p>  ---> 100
		<p th:text="${#numbers.formatDecimal(priceValue,5,10,'POINT')}"></p> ---> 00099.8765400000
	</div>
```
- 문자 관련 : ```#strings```
```
	<h1 th:text="${title}"></h1>
	<span th:utext="${#strings.replace(title,'s','<b>s</b>')}"></span>
	<ul>
		<li th:each="str:${#strings.listSplit(title,' ')}">[[${str}]]</li>
	</ul>
```

# Thymeleaf 링크 처리 예제
- ```@{}``` 를 이용하여 경로에 대한 처리가 가능
```
	<ul>
		<li><a th:href="@{http://localhost:8080/sample1}">sample1</a></li>
		<li><a th:href="@{/sample1}">sample1</a></li>
	</ul>
```
- 파라미터 전달
```
// http://localhost:8080/sample1?p1=aaa&p2=bbb
<li><a th:href="@{/sample1(p1='aaa', p2='bbb')}">sample1</a></li>
```

# Thymeleaf layout dialect와 HTML5 Boilerplate 사용
- Thymeleaf layout dialect 라이브러리 다운
```
// pom.xml
		<!-- https://mvnrepository.com/artifact/nz.net.ultraq.thymeleaf/thymeleaf-layout-dialect -->
		<dependency>
		    <groupId>nz.net.ultraq.thymeleaf</groupId>
		    <artifactId>thymeleaf-layout-dialect</artifactId>
		    <version>2.3.0</version>
		</dependency>
```
- Boilerplate 다운로드 ---> 압축 해제 ---> 프로젝트 내의 static 폴더에 복사
  - https://html5boilerplate.com/
- 프로젝트 내에 templates 폴더 안에 layout 폴더 생성 후 layout1.html 파일 작성
- 해당 layout 사용 (서버 재시작)
```
<html xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{/layout/layout1}">
    ...
</html>
```