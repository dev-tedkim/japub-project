package com.app.japub.common;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

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

}
