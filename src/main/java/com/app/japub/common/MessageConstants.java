package com.app.japub.common;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class MessageConstants {
	public static final String ERROR_MSG = "요청 처리 중 문제가 발생했습니다. 다시 시도해 주세요";
	public static final String SUCCESS_MSG = "요청이 정상적으로 처리되었습니다.";
	public static final String BOARD_NOT_FOUND_MSG = "요청하신 게시글을 찾을 수 없습니다.";
	public static final String ADMIN_NOT_FOUND_MSG = "이 작업은 관리자 권한을 가진 사용자만 수행할 수 있습니다.";
	public static final String USER_NOT_FOUND_MSG = "존재하지 않는 회원 입니다.";
	public static final String CATEGORY_NOT_FOUND_MSG = "카테고리가 없거나 유효하지 않은 카테고리 입니다.";
	public static final String SUCCESS_KEY = "success";
	public static final String SUCCESS_VALUE = "success";
	public static final String LOGIN_URL = "redirect:/login";

	public static void addSuccess(RedirectAttributes attributes) {
		attributes.addFlashAttribute(SUCCESS_KEY, SUCCESS_VALUE);
	}

	public static boolean isSuccess(Model model) {
		return SUCCESS_VALUE.equals(model.getAttribute(SUCCESS_KEY));
	}

	public static void addErrorMessage(RedirectAttributes attributes, String errorMsg) {
		attributes.addFlashAttribute("msg", errorMsg);
	}
}
