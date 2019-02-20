# 인증과 인가
- 인증 (Authentication)
  - 증명하다 의 의미로, 암호가 걸려있는 시스템을 사용한다면, 인증 절차를 거쳐야 한다.
  - 웹에서는 URL은 보안 절차를 거친 사용자들만이 접근할 수 있다는 의미가 된다.
- 인가 (Authorization)
  - 권한 부여나 허가와 같은 의미이다.
  - 보안에서는 어떤 대상이 특정 목적을 실현하도록 Access하는 것을 의미한다.
  - 웹에서는 URL에 접근한 사용자가 특정한 자격이 있다는 것을 의미한다.

# Security 라이브러리 추가 및 엔티티클래스 생성
- 라이브러리 추가하는 부분에 'Security' 라이브러리 추가
- 시큐리티 기본 설정 클래스 파일 생성
```
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		System.out.println("security config");
	}
}

```
- 프로젝트가 정상적으로 수행 된 경우 password를 제공해 준다.
  - 기본 셋팅 : 사용자 이름 = user
  - 비밀 번호 : Using generated security password: ~~
- 엔티티 클래스 작성
  - 회원의 정보를 담고 있는 Member 엔티티 클래스 작성
  - Member 엔티티 클래스에 대해 회원이 가지는 권한에 대한 이름을 가지는 MemberRole 엔티티 클래스 작성
  - Member와 MemberRole 사이에는 일대다 관계로 생성 (Member--->MemberRole 양방향 X)
```
// Member.java
@Entity
@Table(name = "tbl_members")
public class Member {

	@Id
	private String uid;
	
	private String upw;
	
	private String uname;
	
	@CreationTimestamp
	private LocalDateTime regdate;
	@UpdateTimestamp
	private LocalDateTime updatedate;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name = "member")
	private List<MemberRole> roles;

// MemberRole.java
@Entity
@Table(name="tbl_member_roles")
public class MemberRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fno;
	
	private String roleName;
```
- Repository, Controller, View 등을 생성 후 테스트

# Security로 특정 권한을 가진 사람만이 특정 URI에 접근하기
- SecurityConfig 클래스의 configure 메소드에 설정
  - antMatchers를 이용해 경로를 설정
  - permitAll은 모든 사용자의 접근 허용, hasRole은 특정 권한을 가진 사람만 접근을 허용한다.
```
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		System.out.println("security config");
		

		http.authorizeRequests().antMatchers("/guest/**").permitAll();
		http.authorizeRequests().antMatchers("/manager/**").hasRole("MANAGER");
	}
}
```
# Security로 로그인, 권한이 없을 경우 안내 페이지, 로그아웃 기능 구현
- 로그인 구현을 위해 Security 설정 파일에 http.formLogin을 이용한다.
```
	@Override
	protected void configure(HttpSecurity http) throws Exception{

		...

		http.formLogin().loginPage("/login");
	}
```
- html에서 스프링 시큐리티는 기본적으로 input 태그의 name 속성에 username과 password라는 이름을 사용하기 때문에 name 속성을 마음대로 변경할 수 없다.
- 스프링 시큐리티가 적용되면 POST 방식으로 보내는 모든 데이터는 CSRF 토큰 값이 필요하다.
```
<form ...>
	<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
</form>
```
- 권한이 없는 사용자에 대한 안내 페이지 작성
  - Security 설정 파일에서 http.exceptionHandling() 을 이용한다.
```
	@Override
	protected void configure(HttpSecurity http) throws Exception{

		...
		
		http.exceptionHandling().accessDeniedPage("/accessDenied");
	}
```
- 로그아웃 처리하기
  - 세션 정보를 무효화하고, 필요한 경우에는 모든 쿠키를 삭제할 수 있다.
  - 쿠키 삭제 시에는 deleteCookies()를 이용
```
	@Override
	protected void configure(HttpSecurity http) throws Exception{

		...
		
		// 세션 무효화
		http.logout().logoutUrl("/logout").invalidateHttpSession(true);
	}
```

# Security의 인증 매니저 과정
- 모든 인증은 인증 매니저(AuthenticationManager)를 통해서 이루어진다.
- 인증 매니저를 생성하기 위해 인증 매니저 빌더(AuthenticationManagerBuilder)라는 존재가 사용된다.
- 인증 매니저를 이용해서 인증(Authentication) 작업이 수행된다.
- 인증 매니저들을 인증/인가를 위한 UserDetailsService를 통해서 필요한 정보들을 가져온다.
- UserDetails는 사용자의 정보 + 권한 정보들의 묶음이다.

# Security에서 인증 매니저를 위해 UserDetailsService 만들기
- User 클래스를 상속 받는 SecurityUser 클래스 생성
```
public class ZerockSecurityUser extends User {

	private static final String ROLE_PREFIX = "ROLE_";
	
	private Member member;
	
	public ZerockSecurityUser(Member member) {
		
		super(member.getUid(), member.getUpw(), makeGrantedAuthority(member.getRoles()));
		
		this.member = member;
	}
	
	private static List<GrantedAuthority> makeGrantedAuthority(List<MemberRole> roles){
		
		List<GrantedAuthority> list = new ArrayList<>();
		
		roles.forEach(
				role -> list.add(
						new SimpleGrantedAuthority(ROLE_PREFIX + role.getRoleName())
						));
		
		return list;
	}

	// getter and setter
}
```
- 커스텀 UserDetailsService 클래스 만들기
  - JPA 사용을 위해 만들어 둔 Repository와 연동하는 방식
```
@Service
public class ZerockUserService implements UserDetailsService {

	@Autowired
	MemberRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return repo.findById(username)
				.filter(m -> m != null)
				.map(m -> new ZerockSecurityUser(m))
				.get();
	}

}
```
- Security 설정 파일에서 설정하기
```
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	ZerockUserService zerockUserService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{

		...

		http.userDetailsService(zerockUserService);
	}
}
```

# Security 정보를 Thymeleaf에서 사용하기
- Spring Security Dialect 라이브러리 설치
  - 버전 확인 : https://github.com/thymeleaf/thymeleaf-extras-springsecurity
  - maven : https://mvnrepository.com/
```
		<dependency>
		    <groupId>org.thymeleaf.extras</groupId>
		    <artifactId>thymeleaf-extras-springsecurity5</artifactId>
		</dependency>
```
- 프로젝트+Application.java 에 Bean 설정
```
	@Bean
	public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver, SpringSecurityDialect sec) {
	    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	    templateEngine.setTemplateResolver(templateResolver);
	    templateEngine.addDialect(sec); // Enable use of "sec"
	    return templateEngine;
	}
```
- html에서 시큐리티 관련 네임스페이스 추가
```
<html xmlns:th="http://www.thymeleaf.org"
	xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
```
- 해당 네임스페이스를 이용해 태그 작성
- authentication 속성 사용
```
<h3>[[${#authentication.name}]]</h3>
```
- 권한을 가진 사용자에게만 버튼이나 링크가 보이도록 설정
  - admin으로 접속 시 MANAGER AAA 가 보이지 않음
```
	<p sec:authorize-url="/admin/aaa">
		<a href="/admin/aaa">ADMIN AAA</a>
	</p>
	<p sec:authorize-url="/manager/aaa">
		<a href="/manager/aaa">MANAGER AAA</a>
	</p>
```
- 특정 권한을 가진 사용자들에게만 특정 내용 보여주기
```
	<h1 sec:authorize="hasRole('ROLE_ADMIN')">
		This content is only for admin.
	</h1>
```
- SecurityUser 객체의 회원 정보 사용
```
<div>[[${#authentication.principal.member.uname}]]</div>

	<div th:with="member=${#authentication.principal.member}">
		<p>[[${member.uid}]]</p>
		<p>[[${member.upw}]]</p>
		<p>[[${member.uname}]]</p>
	</div>
```

# Security를 이용해 Remember-ME 인증
- 세션 방식
  - 모든 데이터를 서버에서 보관하고, 브라우저는 단순히 Key에 해당하는 세션ID만을 이용하기 때문에 안전하다.
  - 브라우저가 종료되면 사용할 수 없기 때문에 모바일 같은 환경에서는 불편함이 있다.
- 쿠키 방식
  - 브라우저에 일정한 정보를 담은 쿠키를 전송하고, 브라우저는 서버에 접근할 때 주어진 쿠키를 같이 전송한다.
  - 서버에서는 쿠키에 유효기간을 지정할 수 있기 때문에 브라우저가 종료되어도 다음 접근 시 유효기간이 충분하다면 정보를 유지할 수 있다.
  - Remember-ME 기능은 로그인 유지 기능으로 쿠키 방식을 사용한다. (디폴트 2주)
- remember-me 기능 설정
- checkbox에서 id와 name의 속성 값을 remember-me 로 지정한다.
```
	<p>
		<label for="text">Remember-Me</label>
		<input type="checkbox" id="remember-me" name="remember-me"/>
	</p>
```
- 데이터베이스에 쿠키 토큰 값을 저장하기 위한 테이블이 존재해야 한다.
```
create table persistent_logins(
	username varchar(64) not null,
	series varchar(64) primary key,
	token varchar(64) not null,
	last_used timestamp not null
);
```
- rememberMe() 처리를 위해 JdbcTokenRepositoryImpl을 지정해 주어야 한다.
  - security 설정 파일에서 dataSource와 토큰 저장을 위한 Repository 설정
```
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	...

	private PersistentTokenRepository getJDBCRepository() {
		
		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);
		return repo;
	}
}
```
- Security 설정 파일에서 rememberMe() 설정
```
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	...

	@Override
	protected void configure(HttpSecurity http) throws Exception{

		...

		//http.userDetailsService(zerockUserService);
		
		http.rememberMe()
		.key("zerock")
		.userDetailsService(zerockUserService)
		.tokenRepository(getJDBCRepository())
		.tokenValiditySeconds(60*60*24);
	}
}
```

# Security에서 어노테이션을 통해 특정 경로에 접근을 제어하는 방식
- 기존에는 Security 설정 파일에서 configure() 에 접근 제어를 설정하는 방식이었다.
- 스프링 시큐리티는 Secured라는 어노테이션을 통해 접근을 제한하는 방식을 지원한다.
```
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping("/adminSecret")
	public void forAdminSecret() {
		
	}
```
- Security 설정 파일에서 EnableGlobalMethodSecurity 설정
```
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	...
}
```

# Security에서 PasswordEncoder 사용하기
- 암호화에 대한 설명 : https://d2.naver.com/helloworld/318732
  - Pbkdf2, BCrypt, SCrypt
- Security 설정 파일에 PasswordEncoder를 빈에 등록 및 인증 매니저가 이용할 것이라고 명시
```
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	...

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(zerockUserService).passwordEncoder(passwordEncoder());
	}

	...
}
```
- 컨트롤러에서 바로 처리하는 방법 (회원 가입 등)
```
	@Autowired
	PasswordEncoder pwEncoder;
	
	@Autowired
	MemberRepository repo;

	@PostMapping("/join")
	public String joinPost(@ModelAttribute("member") Member member) {
		
		String encryptPw = pwEncoder.encode(member.getUpw());
		member.setUpw(encryptPw);
		repo.save(member);
	}
```
