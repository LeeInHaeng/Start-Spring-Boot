package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

	@GetMapping("/default")
	public void defaultHTML() {
		System.out.println("hello world");
	}
}
