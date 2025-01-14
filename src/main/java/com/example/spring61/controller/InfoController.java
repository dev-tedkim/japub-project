package com.example.spring61.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/info")
public class InfoController {
	
	@GetMapping("/info")
	public void info() {
		
	}
	
	@GetMapping("/map")
	public void map() {
		
	}
	
	@GetMapping("/manuscript")
	public void manuscript() {
	}
	
	@GetMapping("/store-info")
	public void store() {
		
	}
	
	@GetMapping("/exchange")
	public void exchange() {
	}
}
