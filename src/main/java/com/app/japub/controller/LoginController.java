package com.app.japub.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.japub.common.SessionUtil;
import com.app.japub.domain.dto.UserDto;
import com.app.japub.domain.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final UserService userService;
	private final HttpSession session;

	@GetMapping("/login")
	public String login() {
		if (session.getAttribute(SessionUtil.KEY) != null) {
			return "redirect:/main";
		}
		return "login/login";
	}

	@PostMapping("/login")
	public String login(boolean rememberId, String userId, String userPassword, HttpServletResponse resp,
			RedirectAttributes attributes) {
		UserDto userDto = userService.login(userId, userPassword);
		if (userDto == null) {
			String loginFailedMsg = "아이디 또는 비밀번호가 일치하지 않습니다.";
			attributes.addFlashAttribute("msg", loginFailedMsg);
			return "redirect:/login";
		}
		session.setAttribute(SessionUtil.KEY, userDto.getUserNum());
		session.setAttribute(SessionUtil.ADMIN_KEY, userDto.isUserRole());
		setCookie(rememberId, userId, resp);
		return "redirect:/main";
	}

	@GetMapping("/logout")
	public String logout() {
		if (session.getAttribute(SessionUtil.KEY) != null) {
			session.invalidate();
		}
		return "redirect:/login";
	}

	public void setCookie(boolean rememberId, String userId, HttpServletResponse resp) {
		Cookie cookie = new Cookie("id", rememberId ? userId : "");
		cookie.setPath("/");
		cookie.setMaxAge(rememberId ? 60 * 60 * 24 * 7 : 0);
		resp.addCookie(cookie);
	}

}
