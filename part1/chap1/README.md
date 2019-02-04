# Eclipse 환경설정
- Help ---> Eclipse Marketplace ---> Spring Tools 4 - for Spring Boot 설치 (4.4.1) (관리자 권한으로 설치)
- Eclipse 오른쪽 상단의 Perspective에서 Spring을 선택
- File ---> New ---> Spring Starter Project
- Name : boot01 / Group : org.zerock / Package : org.zerock / Packaging : War ---> Next
- Core에서 Lombok 선택, Web에서 Web 선택 ---> Finish
- src/main/java의 프로젝트 이름+Application.java를 Spring Boot App으로 실행
- 간단한 Controller를 만들어 테스트 해보기 (localhost:8080/hello) ---> 톰캣 default 포트 설정 8080
```
// SampleController.java
package org.zerock.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class SampleController {

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello World";
	}
}

```

# 스프링 부트의 장점
- 기존의 스프링 개발 방식에서 불편했던 설정이나 버전 충돌 등의 문제를 없애는 대신에 빠르고 쉬운 서버 환경과 테스트 환경 등을 한 번에 제공
- 자동화된 라이브러리 관리
- Spring Boot Auto Configure
- 적당한 라이브러리 자동 결정과 XML 없는 환경 구축
- 테스트 환경과 내장 Tomcat
