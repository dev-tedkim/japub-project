package com.app.japub.common;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class MessageConstants {
	public static final String ERROR_MSG = "요청 처리 중 문제가 발생했습니다. 다시 시도해 주세요";
	public static final String SUCCESS_MSG = "요청이 정상적으로 처리되었습니다.";
	public static final String BOARD_NOT_FOUND_MSG = "요청하신 게시글을 찾을 수 없습니다.";
	public static final String USER_NOT_FOUND_MSG = "존재하지 않는 회원 입니다.";
	public static final String CATEGORY_NOT_FOUND_MSG = "카테고리가 없거나 유효하지 않은 카테고리 입니다.";
	public static final String BOARD_NO_PERMISSION_MSG = "해당 게시글을 수정하거나 삭제할 수 있는 권한이 없습니다.";
	public static final String JOIN_SUCCESS_MESSAGE = "회원 가입이 완료되었습니다.";
	public static final String LOGIN_ERROR_MSG = "아이디 또는 비밀번호가 일치하지 않습니다.";
	public static final String PASSWORD_UPDATE_SUCCESS_MESSAGE = "비밀번호 변경이 완료되었습니다.";
	public static final String DELETE_ACCOUNT_MSG = "회원 탈퇴가 완료 되었습니다. 그동안 이용해주셔서 감사합니다.";
	public static final String WRONG_PASSWORD_MSG = "비밀번호가 일치하지 않습니다.";
	public static final String ADMIN_NOT_ALLOW_MSG = "이 작업은 관리자 권한을 가진 사용자만 수행할 수 있습니다.";
	public static final String LOGIN_URL = "redirect:/login";
	public static final String KEY_ERROR_MSG = "msg";

	public static void addErrorMessage(RedirectAttributes attributes, String errorMsg) {
		attributes.addFlashAttribute(KEY_ERROR_MSG, errorMsg);
	}
}
