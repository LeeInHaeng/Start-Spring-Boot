# ������ �ΰ�
- ���� (Authentication)
  - �����ϴ� �� �ǹ̷�, ��ȣ�� �ɷ��ִ� �ý����� ����Ѵٸ�, ���� ������ ���ľ� �Ѵ�.
  - �������� URL�� ���� ������ ��ģ ����ڵ鸸�� ������ �� �ִٴ� �ǹ̰� �ȴ�.
- �ΰ� (Authorization)
  - ���� �ο��� �㰡�� ���� �ǹ��̴�.
  - ���ȿ����� � ����� Ư�� ������ �����ϵ��� Access�ϴ� ���� �ǹ��Ѵ�.
  - �������� URL�� ������ ����ڰ� Ư���� �ڰ��� �ִٴ� ���� �ǹ��Ѵ�.

# Security ���̺귯�� �߰� �� ��ƼƼŬ���� ����
- ���̺귯�� �߰��ϴ� �κп� 'Security' ���̺귯�� �߰�
- ��ť��Ƽ �⺻ ���� Ŭ���� ���� ����
```
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		System.out.println("security config");
	}
}

```
- ������Ʈ�� ���������� ���� �� ��� password�� ������ �ش�.
  - �⺻ ���� : ����� �̸� = user
  - ��� ��ȣ : Using generated security password: ~~
- ��ƼƼ Ŭ���� �ۼ�
  - ȸ���� ������ ��� �ִ� Member ��ƼƼ Ŭ���� �ۼ�
  - Member ��ƼƼ Ŭ������ ���� ȸ���� ������ ���ѿ� ���� �̸��� ������ MemberRole ��ƼƼ Ŭ���� �ۼ�
  - Member�� MemberRole ���̿��� �ϴ�� ����� ���� (Member--->MemberRole ����� X)
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
- Repository, Controller, View ���� ���� �� �׽�Ʈ

# Security�� Ư�� ������ ���� ������� Ư�� URI�� �����ϱ�
- SecurityConfig Ŭ������ configure �޼ҵ忡 ����
  - antMatchers�� �̿��� ��θ� ����
  - permitAll�� ��� ������� ���� ���, hasRole�� Ư�� ������ ���� ����� ������ ����Ѵ�.
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
# Security�� �α���, ������ ���� ��� �ȳ� ������, �α׾ƿ� ��� ����
- �α��� ������ ���� Security ���� ���Ͽ� http.formLogin�� �̿��Ѵ�.
```
	@Override
	protected void configure(HttpSecurity http) throws Exception{

		...

		http.formLogin().loginPage("/login");
	}
```
- html���� ������ ��ť��Ƽ�� �⺻������ input �±��� name �Ӽ��� username�� password��� �̸��� ����ϱ� ������ name �Ӽ��� ������� ������ �� ����.
- ������ ��ť��Ƽ�� ����Ǹ� POST ������� ������ ��� �����ʹ� CSRF ��ū ���� �ʿ��ϴ�.
```
<form ...>
	<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
</form>
```
- ������ ���� ����ڿ� ���� �ȳ� ������ �ۼ�
  - Security ���� ���Ͽ��� http.exceptionHandling() �� �̿��Ѵ�.
```
	@Override
	protected void configure(HttpSecurity http) throws Exception{

		...
		
		http.exceptionHandling().accessDeniedPage("/accessDenied");
	}
```
- �α׾ƿ� ó���ϱ�
  - ���� ������ ��ȿȭ�ϰ�, �ʿ��� ��쿡�� ��� ��Ű�� ������ �� �ִ�.
  - ��Ű ���� �ÿ��� deleteCookies()�� �̿�
```
	@Override
	protected void configure(HttpSecurity http) throws Exception{

		...
		
		// ���� ��ȿȭ
		http.logout().logoutUrl("/logout").invalidateHttpSession(true);
	}
```

# Security�� ���� �Ŵ��� ����
- ��� ������ ���� �Ŵ���(AuthenticationManager)�� ���ؼ� �̷������.
- ���� �Ŵ����� �����ϱ� ���� ���� �Ŵ��� ����(AuthenticationManagerBuilder)��� ���簡 ���ȴ�.
- ���� �Ŵ����� �̿��ؼ� ����(Authentication) �۾��� ����ȴ�.
- ���� �Ŵ������� ����/�ΰ��� ���� UserDetailsService�� ���ؼ� �ʿ��� �������� �����´�.
- UserDetails�� ������� ���� + ���� �������� �����̴�.

# Security���� ���� �Ŵ����� ���� UserDetailsService �����
- User Ŭ������ ��� �޴� SecurityUser Ŭ���� ����
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
- Ŀ���� UserDetailsService Ŭ���� �����
  - JPA ����� ���� ����� �� Repository�� �����ϴ� ���
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
- Security ���� ���Ͽ��� �����ϱ�
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

# Security ������ Thymeleaf���� ����ϱ�
- Spring Security Dialect ���̺귯�� ��ġ
  - ���� Ȯ�� : https://github.com/thymeleaf/thymeleaf-extras-springsecurity
  - maven : https://mvnrepository.com/
```
		<dependency>
		    <groupId>org.thymeleaf.extras</groupId>
		    <artifactId>thymeleaf-extras-springsecurity5</artifactId>
		</dependency>
```
- ������Ʈ+Application.java �� Bean ����
```
	@Bean
	public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver, SpringSecurityDialect sec) {
	    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	    templateEngine.setTemplateResolver(templateResolver);
	    templateEngine.addDialect(sec); // Enable use of "sec"
	    return templateEngine;
	}
```
- html���� ��ť��Ƽ ���� ���ӽ����̽� �߰�
```
<html xmlns:th="http://www.thymeleaf.org"
	xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
```
- �ش� ���ӽ����̽��� �̿��� �±� �ۼ�
- authentication �Ӽ� ���
```
<h3>[[${#authentication.name}]]</h3>
```
- ������ ���� ����ڿ��Ը� ��ư�̳� ��ũ�� ���̵��� ����
  - admin���� ���� �� MANAGER AAA �� ������ ����
```
	<p sec:authorize-url="/admin/aaa">
		<a href="/admin/aaa">ADMIN AAA</a>
	</p>
	<p sec:authorize-url="/manager/aaa">
		<a href="/manager/aaa">MANAGER AAA</a>
	</p>
```
- Ư�� ������ ���� ����ڵ鿡�Ը� Ư�� ���� �����ֱ�
```
	<h1 sec:authorize="hasRole('ROLE_ADMIN')">
		This content is only for admin.
	</h1>
```
- SecurityUser ��ü�� ȸ�� ���� ���
```
<div>[[${#authentication.principal.member.uname}]]</div>

	<div th:with="member=${#authentication.principal.member}">
		<p>[[${member.uid}]]</p>
		<p>[[${member.upw}]]</p>
		<p>[[${member.uname}]]</p>
	</div>
```

# Security�� �̿��� Remember-ME ����
- ���� ���
  - ��� �����͸� �������� �����ϰ�, �������� �ܼ��� Key�� �ش��ϴ� ����ID���� �̿��ϱ� ������ �����ϴ�.
  - �������� ����Ǹ� ����� �� ���� ������ ����� ���� ȯ�濡���� �������� �ִ�.
- ��Ű ���
  - �������� ������ ������ ���� ��Ű�� �����ϰ�, �������� ������ ������ �� �־��� ��Ű�� ���� �����Ѵ�.
  - ���������� ��Ű�� ��ȿ�Ⱓ�� ������ �� �ֱ� ������ �������� ����Ǿ ���� ���� �� ��ȿ�Ⱓ�� ����ϴٸ� ������ ������ �� �ִ�.
  - Remember-ME ����� �α��� ���� ������� ��Ű ����� ����Ѵ�. (����Ʈ 2��)
- remember-me ��� ����
- checkbox���� id�� name�� �Ӽ� ���� remember-me �� �����Ѵ�.
```
	<p>
		<label for="text">Remember-Me</label>
		<input type="checkbox" id="remember-me" name="remember-me"/>
	</p>
```
- �����ͺ��̽��� ��Ű ��ū ���� �����ϱ� ���� ���̺��� �����ؾ� �Ѵ�.
```
create table persistent_logins(
	username varchar(64) not null,
	series varchar(64) primary key,
	token varchar(64) not null,
	last_used timestamp not null
);
```
- rememberMe() ó���� ���� JdbcTokenRepositoryImpl�� ������ �־�� �Ѵ�.
  - security ���� ���Ͽ��� dataSource�� ��ū ������ ���� Repository ����
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
- Security ���� ���Ͽ��� rememberMe() ����
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

# Security���� ������̼��� ���� Ư�� ��ο� ������ �����ϴ� ���
- �������� Security ���� ���Ͽ��� configure() �� ���� ��� �����ϴ� ����̾���.
- ������ ��ť��Ƽ�� Secured��� ������̼��� ���� ������ �����ϴ� ����� �����Ѵ�.
```
	@Secured(value={"ROLE_ADMIN"})
	@RequestMapping("/adminSecret")
	public void forAdminSecret() {
		
	}
```
- Security ���� ���Ͽ��� EnableGlobalMethodSecurity ����
```
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	...
}
```

# Security���� PasswordEncoder ����ϱ�
- ��ȣȭ�� ���� ���� : https://d2.naver.com/helloworld/318732
  - Pbkdf2, BCrypt, SCrypt
- Security ���� ���Ͽ� PasswordEncoder�� �� ��� �� ���� �Ŵ����� �̿��� ���̶�� ���
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
- ��Ʈ�ѷ����� �ٷ� ó���ϴ� ��� (ȸ�� ���� ��)
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
