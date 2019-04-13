package com.seuit.spring.watchshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	@GetMapping(value= {"/"})
	public String showIndex() {
		return "index";
	}
	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}
	
	@GetMapping("/admin")
	public String showAdminIndex() {
		return "admin/index";
	}
	
	@GetMapping("/manager")
	public String showIndexManager() {
		return "manager/index";
	}
	
	@GetMapping("/logout")
	public String execLogout() {
		return "login";
	}
	@RequestMapping("/403")
	public String accessDenied() {
	    return "exception/403";
	}
	
	@GetMapping("/register")
	public String showRegisterPage() {
	    return "register";
	}
}
