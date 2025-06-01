package com.app.japub.common;

import javax.servlet.http.HttpSession;

public class SessionUtil {

	public static final String KEY = "userNum";
	public static final String ADMIN_KEY = "isAdmin";

	public static boolean isAdmin(HttpSession session) {
		Object isAdmin = session.getAttribute(SessionUtil.ADMIN_KEY);
		return isAdmin != null && (boolean) isAdmin;
	}

}
