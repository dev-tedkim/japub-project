package com.example.spring61.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring61.domain.dto.UserDto;
import com.example.spring61.domain.service.user.MailServiceImpl;
import com.example.spring61.domain.service.user.PasswordServiceImpl;
import com.example.spring61.domain.service.user.UserService;
import com.example.spring61.domain.validator.UserValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final MailServiceImpl mailService;
	private final PasswordServiceImpl passwordService;
	private final String ERROR_MSG = "요청처리중 문제가 발생했습니다. 다시 시도해 주세요";

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new UserValidator());
	}

	@GetMapping("/term")
	public void term() {

	}

	@GetMapping("/join")
	public void join(UserDto userDto) {

	}

	@PostMapping("/join")
	public String join(@Valid UserDto userDto, BindingResult result, RedirectAttributes attributes) {
		userDto.setUserPhoneNumber(userDto.getUserPhoneNumber().replaceAll("-", ""));
		if (result.hasErrors()) {
			return "user/join";
		}
		if (!userService.insert(userDto)) {
			attributes.addFlashAttribute("user", userDto);
			attributes.addFlashAttribute("msg", ERROR_MSG);
			return "redirect:/user/join";
		}
		attributes.addFlashAttribute("msg", "회원가입이 완료 되었습니다.");
		return "redirect:/user/login";
	}

	@GetMapping("/login")
	public String login(UserDto userDto, HttpSession session) {
		if (session.getAttribute("userNum") != null) {
			return "redirect:/main";
		}
		return "user/login";
	}

	@PostMapping("/login")
	public String login(String userId, String userPassword, boolean rememberId, HttpSession session,
			HttpServletResponse resp, RedirectAttributes attributes) {
		UserDto userDto = userService.findByUserIdAndUserPassword(userId, userPassword);
		if (userDto == null) {
			attributes.addFlashAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
			return "redirect:/user/login";
		}
		session.setAttribute("userNum", userDto.getUserNum());
		setLoginCookie(userId, rememberId, resp);
		return "redirect:/main";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/main";
	}

	@GetMapping("/checkPassword")
	public String checkPassword(HttpSession session) {
		if (session.getAttribute("userNum") == null) {
			return "redirect:/user/login";
		}
		return "user/checkPassword";
	}

	@PostMapping("/checkPassword")
	public String checkPassword(String userPassword, HttpSession session, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute("userNum");
		UserDto userDto = userService.checkPassword(userNum, userPassword);
		if (userDto == null) {
			attributes.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
			return "redirect:/user/checkPassword";
		}
		return "redirect:/user/mypage?userNum=" + userDto.getUserNum();
	}

	@GetMapping("/mypage")
	public String mypage(Long userNum, HttpSession session, RedirectAttributes attributes, Model model) {
		Long sessionUserNum = (Long) session.getAttribute("userNum");
		UserDto userDto = userService.findByUserNum(userNum);
		if (!userNum.equals(sessionUserNum) && userDto == null) {
			attributes.addFlashAttribute("msg", ERROR_MSG);
			return "redirect:/user/checkPassword";
		}
		model.addAttribute("user", userDto);
		return "user/mypage";
	}

	@PostMapping("/mypage")
	public String mypage(UserDto userDto, RedirectAttributes attributes, HttpSession session) {

		if (!userService.update(userDto)) {
			attributes.addFlashAttribute("msg", ERROR_MSG);
			return "redirect:/user/mypage?userNum=" + userDto.getUserNum();
		}
		session.invalidate();
		return "redirect:/user/login";
	}

	@GetMapping("/find")
	public void find() {

	}

	@PostMapping(value = "/find", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> find(@RequestBody UserDto userDto) {
		UserDto user = userService.findByUserEmail(userDto.getUserEmail());
		if (user == null) {
			return new ResponseEntity<String>("일치하는 회원정보가 없습니다.", HttpStatus.OK);
		}
		try {
			if (userService.resetPasswordAndSendMail(user)) {
				return new ResponseEntity<String>("아이디와 비밀번호가 전송되었습니다. 로그인 후 비밀번호를 반드시 재설정해주세요.", HttpStatus.OK);
			}
		} catch (Exception e) {}
		return new ResponseEntity<String>("메일전송 중 오류가 발생했습니다. 잠시후 다시 시도해주세요.", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping("/checkId")
	public ResponseEntity<Boolean> checkId(String userId) {
		if (userService.findByUserId(userId) == null) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}

	private void setLoginCookie(String userId, boolean rememberId, HttpServletResponse resp) {
		Cookie cookie = new Cookie("id", rememberId ? userId : "");
		cookie.setPath("/");
		cookie.setMaxAge(rememberId ? 60 * 60 * 24 * 7 : 0);
		resp.addCookie(cookie);
	}

}
