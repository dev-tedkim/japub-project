package com.example.spring61.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error")
public class ErrorController {

	@GetMapping("/{errorCode}")
	public ModelAndView errorHandler(@PathVariable int errorCode) {
		ModelAndView modelAndView = null;
		if (HttpStatus.BAD_REQUEST.value() == errorCode) {
			modelAndView = new ModelAndView("error/400", HttpStatus.BAD_REQUEST);
		} else if (HttpStatus.NOT_FOUND.value() == errorCode) {
			modelAndView = new ModelAndView("error/404", HttpStatus.NOT_FOUND);
		} else {
			modelAndView = new ModelAndView("error/500", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return modelAndView;
	}
}
