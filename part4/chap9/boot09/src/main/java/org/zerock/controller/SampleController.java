package org.zerock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.persistence.MemberRepository;

@Controller
public class SampleController {
		
	@GetMapping("/")
	public String index() {
		System.out.println("index");
		return "index";
	}
	
	@GetMapping("/login")
	public void login() {
		
	}
	
	@GetMapping("/accessDenied")
	public void accessDenied() {
		
	}
	
	@GetMapping("/logout")
	public void logout() {
		
	}
	
	@RequestMapping("/guest")
	public void forGuest() {
		System.out.println("guest");
	}
	
	@RequestMapping("/manager")
	public void forManager() {
		System.out.println("manager");
	}
	
	@RequestMapping("/admin")
	public void forAdmin() {
		System.out.println("admin");
	}
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping("/adminSecret")
	public void forAdminSecret() {
		
	}
}
