package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
public class HelloController {
	@GetMapping(value = "/index")
	public String mainPage(ModelMap model, Principal principal) {
		if (principal != null) {
			model.addAttribute("username", principal.getName());
			model.addAttribute("roles",
					((Authentication)principal).getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
		}
		return "index";
	}
}