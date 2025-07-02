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
	private static final String KEY_IS_DELETE = "isDelete";
	private static final String KEY_USER = "user";

	@GetMapping("/check-password")
	public String checkPassword(boolean isDelete, Model model) {
		if (SessionUtil.getSessionNum(session) == null) {
			return MessageConstants.LOGIN_URL;
		}
		addIsDeleteInModel(model, isDelete);
		return "mypage/check-password";
	}

	@PostMapping("check-password")
	public String checkPassword(String userPassword, boolean isDelete, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		UserDto userDto = userService.findByUserNumAndUserPassword(userNum, userPassword);
		if (userDto == null) {
			String wrongPassword = "잘못된 비밀번호 입니다.";
			MessageConstants.addErrorMessage(attributes, wrongPassword);
			addIsDeleteQueryParam(attributes, isDelete);
			return "redirect:/mypage/check-password";
		}
		MessageConstants.addSuccess(attributes);
		return isDelete ? "redirect:/mypage/delete" : "redirect:/mypage/update";
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
	public String update(UserDto userDto, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		userDto.setUserNum(userNum);
		if (!userService.update(userDto)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.USER_NOT_FOUND_MSG);
			MessageConstants.addSuccess(attributes);
			return "redirect:/mypage/update";
		}
		session.invalidate();
		return MessageConstants.LOGIN_URL;
	}

	@PostMapping("/delete")
	public String delete(Long userNum, RedirectAttributes attributes) {
		Long sessionUserNum = SessionUtil.getSessionNum(session);
		if (sessionUserNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		if (userNum == null || !sessionUserNum.equals(userNum) || !userService.delete(userNum)) {
			addIsDeleteQueryParam(attributes, true);
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return "redirect:/mypage/check-password";
		}
		String msg = "회원 탈퇴가 정상적으로 처리되었습니다.";
		MessageConstants.addErrorMessage(attributes, msg);
		session.invalidate();
		return MessageConstants.LOGIN_URL;
	}

	private String resolveView(RedirectAttributes attributes, Model model, boolean isDelete) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		boolean isSuccess = MessageConstants.isSuccess(model);
		if (!isSuccess) {
			addIsDeleteQueryParam(attributes, isDelete);
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return "redirect:/mypage/check-password";
		}
		UserDto userDto = userService.findByUserNum(userNum);
		if (userDto == null) {
			addIsDeleteQueryParam(attributes, isDelete);
			MessageConstants.addErrorMessage(attributes, MessageConstants.USER_NOT_FOUND_MSG);
			return "redirect:/mypage/check-password";
		}
		addUserInModel(model, userDto);
		return isDelete ? "mypage/delete" : "mypage/update";
	}

	private void addIsDeleteInModel(Model model, boolean isDelete) {
		model.addAttribute(KEY_IS_DELETE, isDelete);
	}

	private void addIsDeleteQueryParam(RedirectAttributes attributes, boolean isDelete) {
		attributes.addAttribute(KEY_IS_DELETE, isDelete);
	}

	private void addUserInModel(Model model, UserDto userDto) {
		model.addAttribute(KEY_USER, userDto);
	}
}
