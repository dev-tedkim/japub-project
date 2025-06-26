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

import com.app.japub.common.SessionUtil;
import com.app.japub.domain.dto.UserDto;
import com.app.japub.domain.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/find-account")
@RequiredArgsConstructor
@Controller
public class FindAccountController {
	private final UserService userService;
	private final HttpSession session;

	@GetMapping()
	public String findAccount() {
		if (session.getAttribute(SessionUtil.KEY) != null) {
			session.invalidate();
		}
		return "find/find-account";
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> findAccount(@RequestBody UserDto userDto) {
		userDto = userService.findByUserEmail(userDto.getUserEmail());

		if (userDto == null) {
			String msg = "존재하지 않는 회원입니다.";
			return new ResponseEntity<String>(msg, HttpStatus.NOT_FOUND);
		}
		try {
			userService.setUserPasswordAndSendMail(userDto);
			String msg = "입력하신 이메일로 아이디와 임시 비밀번호를 전송하였습니다.";
			return new ResponseEntity<String>(msg, HttpStatus.OK);
		} catch (Exception e) {
			String msg = "메일 전송중 오류가 발생하였습니다 잠시후 다시 시도해 주세요.";
			return new ResponseEntity<String>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
