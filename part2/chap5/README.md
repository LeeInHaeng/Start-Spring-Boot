# Thymeleaf (springboot ���ø�) ���
- ��� Dependency
  - DevTools, Thymeleaf, Web
  - src/main/resources���� static�� js, css, html, �̹��� ���� / templates�� Thymeleaf�� �̿��� ���ø���
- Window ---> Preferences ---> General ---> Workspace���� utf-8�� ���ڵ� ����
- ���� �� ĳ�̵Ǵ� �������� �����ϱ� ���� ĳ�ø� false�� ����
```
// application.properties
spring.thymeleaf.cache=false
```
- ���ø��� �̿��ϱ� ���� ��Ʈ�ѷ� �ۼ�
```
@Controller
public class SampleController {

	@GetMapping("/sample1")
	public void sample1(Model model) {
		model.addAttribute("greeting", "Hello World");
	}
}
```
- Thymeleaf ��� ���Ǹ� ���� ���� �÷����� ��ġ
  - https://github.com/thymeleaf/thymeleaf-extras-eclipse-plugin ���� Installation �κ� ����
  - eclipse���� help ---> install new softwares �޴����� github�� �����ִ� URL �߰�
  - Name : Thymeleaf / Location : http://www.thymeleaf.org/eclipse-plugin-update-site/
- src/main/resources/templates���� html ���� �ۼ�
  - ���� ���� Thymeleaf�� ����Ѵٴ� ���� ����� �ְ�, html�� �ۼ��Ѵ�.
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

# Thymeleaf (springboot ���ø�) �⺻ ���� ����
- th:text = �±��� ���빰�� ���ڿ��� ���
```
<h1 th:text="${vo}"></h1>
```
- th:utext = ���ڿ��� �ƴ� HTML ��ü�� ����ϴµ� ���
- th:each = ����Ʈ, Iterable, Map, �迭 ���� ����� �� �ִ�.
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
- th:each = iterable�� �̿��� �������� ���� Ȯ�� ����
  - index : 0���� �����ϴ� �ε��� ��ȣ
  - count : 1���� �����ϴ� ��ȣ
  - size : ���� ����� length Ȥ�� size
  - odd/even : ���� ��ȣ�� Ȧ��/¦�� ���� (true, false)
  - first/last : ó�� ������� ������ ������� �Ǵ� (true, false)
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
- th:with = Ư���� ���������� ��ȿ�� ���������� ����
```
<table border="1" th:with="target='u0001'">
```
- th:if ~ th:unless = if ~ else ó���� ���� ���
```
<tr th:each="member, iterState : ${list}">
	<td th:if="${member.mid}">
		<a href="/modify" th:if="${member.mid == target}">MODIFY</a>
		<p th:unless="${member.mid == target}">VIEW</p>
	</td>
</tr>
```
- th:inline = javascript�� ����ϱ� ���� ���
  - model���� �Ѿ�� ������ "${����}" �� �����ϴ� ���� �ƴ� "[[${����}]]" �� �����Ѵ�.
```
	<script th:inline="javascript">
		var result = [[${result}]];
		console.log(result);
	</script>
```
- th:fragment = �Ϻκи� ���ø� ��ü�� ���� ���
```
<div th:fragment="footer">
	Footer �����Դϴ�.
</div>
```
- th:insert = fragment�� ��Ŭ��� ��Ű�� ���� ��� {���::fragment}
```
	<div th:insert="~{fragments/header::header}"></div>
	<div th:insert="~{fragments/footer::footer}"></div>
```

# Thymeleaf ��ƿ��Ƽ ��ü ��� ����
- ��¥ ���� : ```#dates```
```
	<h2 th:text="${#dates.format(now, 'yyyy-MM-dd')}"></h2>
	<div th:with="timeValue=${#dates.createToday()}">
		<p>[[${timeValue}]]</p>
	</div>
```
- ���� ���� : ```#numbers```
```
	<h2 th:text="${#numbers.formatInteger(price,3,'COMMA')}"></h2>
	<div th:with="priceValue=99.87654">
		<p th:text="${#numbers.formatInteger(priceValue,3,'COMMA')}"></p>  ---> 100
		<p th:text="${#numbers.formatDecimal(priceValue,5,10,'POINT')}"></p> ---> 00099.8765400000
	</div>
```
- ���� ���� : ```#strings```
```
	<h1 th:text="${title}"></h1>
	<span th:utext="${#strings.replace(title,'s','<b>s</b>')}"></span>
	<ul>
		<li th:each="str:${#strings.listSplit(title,' ')}">[[${str}]]</li>
	</ul>
```

# Thymeleaf ��ũ ó�� ����
- ```@{}``` �� �̿��Ͽ� ��ο� ���� ó���� ����
```
	<ul>
		<li><a th:href="@{http://localhost:8080/sample1}">sample1</a></li>
		<li><a th:href="@{/sample1}">sample1</a></li>
	</ul>
```
- �Ķ���� ����
```
// http://localhost:8080/sample1?p1=aaa&p2=bbb
<li><a th:href="@{/sample1(p1='aaa', p2='bbb')}">sample1</a></li>
```

# Thymeleaf layout dialect�� HTML5 Boilerplate ���
- Thymeleaf layout dialect ���̺귯�� �ٿ�
```
// pom.xml
		<!-- https://mvnrepository.com/artifact/nz.net.ultraq.thymeleaf/thymeleaf-layout-dialect -->
		<dependency>
		    <groupId>nz.net.ultraq.thymeleaf</groupId>
		    <artifactId>thymeleaf-layout-dialect</artifactId>
		    <version>2.3.0</version>
		</dependency>
```
- Boilerplate �ٿ�ε� ---> ���� ���� ---> ������Ʈ ���� static ������ ����
  - https://html5boilerplate.com/
- ������Ʈ ���� templates ���� �ȿ� layout ���� ���� �� layout1.html ���� �ۼ�
- �ش� layout ��� (���� �����)
```
<html xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{/layout/layout1}">
    ...
</html>
```