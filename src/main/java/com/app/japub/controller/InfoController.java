package com.app.japub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/info")
public class InfoController {
	
	@GetMapping()
	public String info() {
		return "info/info";
	}
	
	@GetMapping("/map")
	public void map() {
		
	}
	
	@GetMapping("/manu-script")
	public void manuscript() {
	}
	
	@GetMapping("/store-info")
	public void store() {
		
	}
	
	@GetMapping("/exchange")
	public void exchange() {
	}
}
