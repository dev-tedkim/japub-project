
package com.app.japub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error")
public class HttpErrorController {

	@GetMapping("/{errorCode}")
	public ModelAndView errorHandler(@PathVariable int errorCode) {
		ModelAndView modelAndView = new ModelAndView();
		if (HttpStatus.BAD_REQUEST.value() == errorCode) {
			modelAndView.setStatus(HttpStatus.BAD_REQUEST);
		} else if (HttpStatus.NOT_FOUND.value() == errorCode) {
			modelAndView.setStatus(HttpStatus.NOT_FOUND);
		} else if (HttpStatus.METHOD_NOT_ALLOWED.value() == errorCode) {
			modelAndView.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
		} else if (HttpStatus.UNSUPPORTED_MEDIA_TYPE.value() == errorCode) {
			modelAndView.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		} else {
			modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		modelAndView.setViewName("error/error");
		return modelAndView;
	}

}
