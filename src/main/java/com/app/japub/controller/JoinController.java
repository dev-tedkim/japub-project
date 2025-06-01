package com.app.japub.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.japub.common.MessageConstants;
import com.app.japub.common.SessionUtil;
import com.app.japub.domain.dto.UserDto;
import com.app.japub.domain.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/join")
@Controller
@RequiredArgsConstructor
public class JoinController {
	private final UserService userService;
	private final HttpSession session;
	private static final String TERM_KEY = "success";

	@GetMapping("/term")
	public String term() {
		if (session.getAttribute(SessionUtil.KEY) != null) {
			session.invalidate();
		}
		return "join/term";
	}

	@PostMapping("/term")
	public String termCheck() {
		if (session.getAttribute(SessionUtil.KEY) != null) {
			session.invalidate();
		}
		session.setAttribute(TERM_KEY, true);
		return "redirect:/join";
	}

	@GetMapping()
	public String join() {
		boolean result = Boolean.valueOf(String.valueOf(session.getAttribute(TERM_KEY)));
		if (!result) {
			return "redirect:/join/term";
		}
		session.invalidate();
		return "join/join";
	}

	@PostMapping()
	public String join(UserDto userDto, RedirectAttributes attributes) {
		if (!userService.insert(userDto)) {
			attributes.addFlashAttribute("user", userDto);
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			return "redirect:/join";
		}
		return "redirect:/login";
	}

	@PostMapping(value = "/checkId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkId(@RequestBody UserDto userDto) {
		Boolean result = userService.findByUserId(userDto.getUserId()) == null;
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@PostMapping(value = "/checkEmail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkEmail(@RequestBody UserDto userDto) {
		Boolean result = userService.findByUserEmail(userDto.getUserEmail()) == null;
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}
