package com.app.japub.common;

import com.app.japub.domain.dto.Criteria;

public class ViewPathUtil {
	public static String getRedirectPath(Criteria criteria, String basePath, String subPath) {
		String params = criteria == null ? "" : criteria.getParams();
		return "redirect:/" + basePath + "/" + subPath + params;
	}

	public static String getForwardPath(String basePath, String subPath) {
		return basePath + "/" + subPath;
	}
}
