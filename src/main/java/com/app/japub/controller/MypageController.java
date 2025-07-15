package com.app.japub.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.japub.common.KeyUtil;
import com.app.japub.common.MessageConstants;
import com.app.japub.common.SessionUtil;
import com.app.japub.domain.dto.UserDto;
import com.app.japub.domain.service.user.UserService;

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
		addIsDeleteToModel(model, isDelete);
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
			MessageConstants.addErrorMessage(attributes, MessageConstants.WRONG_PASSWORD_MSG);
			addIsDeleteToQuery(attributes, isDelete);
			return "redirect:/mypage/check-password";
		}
		KeyUtil.addSuccessToFlash(attributes);
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
			KeyUtil.addSuccessToFlash(attributes);
			return "redirect:/mypage/update";
		}
		session.invalidate();
		MessageConstants.addErrorMessage(attributes, MessageConstants.PASSWORD_UPDATE_SUCCESS_MESSAGE);
		return MessageConstants.LOGIN_URL;
	}

	@PostMapping("/delete")
	public String delete(Long userNum, RedirectAttributes attributes) {
		Long sessionUserNum = SessionUtil.getSessionNum(session);
		if (sessionUserNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		if (userNum == null || !sessionUserNum.equals(userNum) || !userService.delete(userNum)) {
			addIsDeleteToQuery(attributes, true);
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return "redirect:/mypage/check-password";
		}
		MessageConstants.addErrorMessage(attributes, MessageConstants.DELETE_ACCOUNT_MSG);
		session.invalidate();
		return MessageConstants.LOGIN_URL;
	}

	private String resolveView(RedirectAttributes attributes, Model model, boolean isDelete) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}

		boolean isSuccess = KeyUtil.isSuccess(model);
		if (!isSuccess) {
			addIsDeleteToQuery(attributes, isDelete);
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return "redirect:/mypage/check-password";
		}
		UserDto userDto = userService.findByUserNum(userNum);
		if (userDto == null) {
			addIsDeleteToQuery(attributes, isDelete);
			MessageConstants.addErrorMessage(attributes, MessageConstants.USER_NOT_FOUND_MSG);
			return "redirect:/mypage/check-password";
		}
		addUserToModel(model, userDto);
		return isDelete ? "mypage/delete" : "mypage/update";
	}

	private void addIsDeleteToModel(Model model, boolean isDelete) {
		model.addAttribute(KEY_IS_DELETE, isDelete);
	}

	private void addIsDeleteToQuery(RedirectAttributes attributes, boolean isDelete) {
		attributes.addAttribute(KEY_IS_DELETE, isDelete);
	}

	private void addUserToModel(Model model, UserDto userDto) {
		model.addAttribute(KEY_USER, userDto);
	}
}
