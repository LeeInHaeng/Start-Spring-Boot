# CSRF 이해
- 공격 방식 설명 : https://m.blog.naver.com/PostView.nhn?blogId=limjongmin15&logNo=40161766681&proxyReferer=https%3A%2F%2Fwww.google.co.kr%2F

# Thymeleaf에서 Security를 이용해 익명 사용자 체크
- 로그인 하지 않은 사용자라면 uid에 Guest를, 로그인 한 사용자라면 Member 객체의 uid를 사용
```
<div th:with="uid =${#authentication.principal} eq 'anonymousUser' ?
			'Guest' : ${#authentication.principal.member.uid} ">
```
- uid를 통해 조건 검사
```
<a th:if="${uid} eq ${vo.writer}"
```
# Ajax 처리 전에 익명 사용자 검사 (Javascript)
- 의미 없는 로그인 form 작성
```
<form th:action="${'/login'}"></form>
```
- javascript에서 익명 사용자 검사
  - 익명 사용자라면 login 하도록 이동
```
var uid=[[${#authentication.principal} eq 'anonymousUser' ? 
		null : ${#authentication.principal.member.uid}]];

$("#addReplyBtn").on("click", () => {
					
	if(uid==null){
		if(confirm("로그인할까요?")){
			self.location = [[@{/login}]]+"?dest=" + encodeURIComponent(self.location);
		}
		return;
	}
	...
}
```
- Ajax 처리 이므로 Javascript에서 CSRF 토큰 전송
```
var csrf = JSON.parse('[[${_csrf}]]');
var obj = {replyText: replyText, replyer: replyer, bno: bno, csrf:csrf};

		// ajax 처리
		$.ajax({
			type : 'post',
			url : '/replies/' + obj.bno,
			data : JSON.stringify(obj),
			dataType : 'json',
			beforeSend : function(xhr){
					xhr.setRequestHeader(obj.csrf.headerName, obj.csrf.token);
				},
			contentType : "application/json",
			success : callback
		});
```

# 컨트롤러의 인터셉터 사용
- 인터셉터는 컨트롤러의 호출을 사전 혹은 사후에 가로챌 수 있다.
- 또한 서블릿 관련 자원들을 그대로 활용할 수 있다는 장점이 있다.
- preHandle()은 컨트롤러의 호출 전에 동작한다.
- 파라미터의 이름 중 dest가 존재한다면 이를 세션에 저장한다.
```
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String dest = request.getParameter("dest");
		
		if(dest != null) {
			request.getSession().setAttribute("dest", dest);
		}
		
		return super.preHandle(request, response, handler);
	}
	
}
```
- 인터셉터 설정을 위한 클래스 파일을 추가한다.
  - "/login" 경로 호출 시 위에서 구현한 LoginCheckInterceptor가 동작하도록 설정한다.
```
public class InterceptorConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginCheckInterceptor()).addPathPatterns("/login");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	
}
```
- Security에서 로그인 성공 후 리다이렉트를 위해 핸들러를 구현한다.
  - SavedRequestAwareAuthenticationSuccessHandler 를 이용한다.
  - 세션에 dest 값이 존재할 경우 리다이렉트 경로를 dest 값으로 지정한다.
  - 세션에 dest가 존재하지 않는 경우에는 기존 방식으로 동작한다.
```
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
		
		Object dest = request.getSession().getAttribute("dest");
		
		String nextURL = null;
		
		if(dest != null) {
			request.getSession().removeAttribute("dest");
			nextURL = (String)dest;
		}
		else {
			nextURL = super.determineTargetUrl(request, response);
		}
		
		return nextURL;
	}
}
```
- Security 설정 파일에 로그인 성공 후 동작할 핸들러를 설정한다.
```
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		...
		http.formLogin().loginPage("/login").successHandler(new LoginSuccessHandler());
		...
	}
```
- 리다이렉트가 필요한 view 부분에 dest 파라미터가 추가되도록 설정한다.
```
self.location = [[@{/login}]]+"?dest=" + encodeURIComponent(self.location);
```
