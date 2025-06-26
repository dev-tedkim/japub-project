package com.app.japub.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.japub.common.MessageConstants;
import com.app.japub.common.SessionUtil;
import com.app.japub.domain.dto.UserDto;
import com.app.japub.domain.service.user.UserService;
import com.google.protobuf.Message;

import lombok.RequiredArgsConstructor;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MypageController {
	private final UserService userService;
	private final HttpSession session;

	@GetMapping("/check-password")
	public String checkPassword(String action, Model model) {
		if (SessionUtil.getSessionNum(session) == null) {
			return "redirect:/login";
		}
		model.addAttribute("delete", action);
		return "mypage/check-password";
	}

	@PostMapping("/check-password")
	public String checkPassword(String userPassword, RedirectAttributes attributes, String action) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return "redirect:/mypage/login";
		}
		UserDto userDto = userService.findByUserNumAndUserPassword(userNum, userPassword);
		if (userDto == null) {
			String wrongPassword = "잘못된 비밀번호 입니다.";
			MessageConstants.addErrorMessage(attributes, wrongPassword);
			return "redirect:/mypage/check-password";
		}
		MessageConstants.addSuccess(attributes);
		if (action != null && "delete".equals(action)) {
			return "redirect:/mypage/delete";
		}
		return "redirect:/mypage/update";
	}

	@GetMapping("/update")
	public String update(RedirectAttributes attributes, Model model) {
		return resolveView(attributes, model, false);
	}

	@GetMapping("/delete")
	public String delete(RedirectAttributes attributes, Model model) {
		return resolveView(attributes, model, true);
	}

	@PostMapping("/update")
	public String mypage(UserDto userDto, RedirectAttributes attributes) {
		if (SessionUtil.getSessionNum(session) == null) {
			return "redirect:/login";
		}
		if (!userService.update(userDto)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			MessageConstants.addSuccess(attributes);
			return "redirect:/mypage/update";
		}
		session.invalidate();
		return "redirect:/login";
	}

	@PostMapping("/delete")
	public String delete(UserDto userDto, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return "redirect:/login";
		}
		if (!userService.delete(userNum)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			MessageConstants.addSuccess(attributes);
			return "redirect:/mypage/delete";
		} else {
			session.invalidate();
			String msg = "회원탈퇴가 완료 되었습니다.";
			MessageConstants.addErrorMessage(attributes, msg);
			return "redirect:/login";
		}
	}

	public String resolveView(RedirectAttributes attributes, Model model, boolean isDelete) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return "redirect:/login";
		}
		boolean isSuccess = MessageConstants.isSuccess(model);
		if (!isSuccess) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return isDelete ? "redirect:/mypage/check-password?action=delete" : "redirect:/mypage/check-password";
		}
		UserDto userDto = userService.findByUserNum(userNum);
		if (userDto == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return "redirect:/mypage/check-password";
		}
		model.addAttribute("user", userDto);
		return isDelete ? "mypage/delete" : "mypage/update";
	}

}
