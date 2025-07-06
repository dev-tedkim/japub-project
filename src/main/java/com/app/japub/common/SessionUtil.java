package com.app.japub.common;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.app.japub.domain.dto.UserDto;

public class SessionUtil {

	public static final String KEY = "userNum";
	public static final String ADMIN_KEY = "isAdmin";

	public static boolean isAdmin(HttpSession session) {
		Boolean isAdmin = (Boolean) session.getAttribute(ADMIN_KEY);
		return Boolean.TRUE.equals(isAdmin);
	}

	public static Long getSessionNum(HttpSession session) {
		return (Long) session.getAttribute(KEY);
	}

	public static void addIsAdminToModel(Model model, HttpSession session) {
		model.addAttribute(ADMIN_KEY, isAdmin(session));

	}

	public static void addUserNumInSession(HttpSession session, UserDto userDto) {
		session.setAttribute(KEY, userDto.getUserNum());
	}

	public static void addIsAdminInSession(HttpSession session, UserDto userDto) {
		session.setAttribute(ADMIN_KEY, userDto.isUserRole());
	}

}
