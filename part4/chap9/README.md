# CSRF ����
- ���� ��� ���� : https://m.blog.naver.com/PostView.nhn?blogId=limjongmin15&logNo=40161766681&proxyReferer=https%3A%2F%2Fwww.google.co.kr%2F

# Thymeleaf���� Security�� �̿��� �͸� ����� üũ
- �α��� ���� ���� ����ڶ�� uid�� Guest��, �α��� �� ����ڶ�� Member ��ü�� uid�� ���
```
<div th:with="uid =${#authentication.principal} eq 'anonymousUser' ?
			'Guest' : ${#authentication.principal.member.uid} ">
```
- uid�� ���� ���� �˻�
```
<a th:if="${uid} eq ${vo.writer}"
```
# Ajax ó�� ���� �͸� ����� �˻� (Javascript)
- �ǹ� ���� �α��� form �ۼ�
```
<form th:action="${'/login'}"></form>
```
- javascript���� �͸� ����� �˻�
  - �͸� ����ڶ�� login �ϵ��� �̵�
```
var uid=[[${#authentication.principal} eq 'anonymousUser' ? 
		null : ${#authentication.principal.member.uid}]];

$("#addReplyBtn").on("click", () => {
					
	if(uid==null){
		if(confirm("�α����ұ��?")){
			self.location = [[@{/login}]]+"?dest=" + encodeURIComponent(self.location);
		}
		return;
	}
	...
}
```
- Ajax ó�� �̹Ƿ� Javascript���� CSRF ��ū ����
```
var csrf = JSON.parse('[[${_csrf}]]');
var obj = {replyText: replyText, replyer: replyer, bno: bno, csrf:csrf};

		// ajax ó��
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

# ��Ʈ�ѷ��� ���ͼ��� ���
- ���ͼ��ʹ� ��Ʈ�ѷ��� ȣ���� ���� Ȥ�� ���Ŀ� ����ç �� �ִ�.
- ���� ���� ���� �ڿ����� �״�� Ȱ���� �� �ִٴ� ������ �ִ�.
- preHandle()�� ��Ʈ�ѷ��� ȣ�� ���� �����Ѵ�.
- �Ķ������ �̸� �� dest�� �����Ѵٸ� �̸� ���ǿ� �����Ѵ�.
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
- ���ͼ��� ������ ���� Ŭ���� ������ �߰��Ѵ�.
  - "/login" ��� ȣ�� �� ������ ������ LoginCheckInterceptor�� �����ϵ��� �����Ѵ�.
```
public class InterceptorConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginCheckInterceptor()).addPathPatterns("/login");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	
}
```
- Security���� �α��� ���� �� �����̷�Ʈ�� ���� �ڵ鷯�� �����Ѵ�.
  - SavedRequestAwareAuthenticationSuccessHandler �� �̿��Ѵ�.
  - ���ǿ� dest ���� ������ ��� �����̷�Ʈ ��θ� dest ������ �����Ѵ�.
  - ���ǿ� dest�� �������� �ʴ� ��쿡�� ���� ������� �����Ѵ�.
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
- Security ���� ���Ͽ� �α��� ���� �� ������ �ڵ鷯�� �����Ѵ�.
```
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		...
		http.formLogin().loginPage("/login").successHandler(new LoginSuccessHandler());
		...
	}
```
- �����̷�Ʈ�� �ʿ��� view �κп� dest �Ķ���Ͱ� �߰��ǵ��� �����Ѵ�.
```
self.location = [[@{/login}]]+"?dest=" + encodeURIComponent(self.location);
```
