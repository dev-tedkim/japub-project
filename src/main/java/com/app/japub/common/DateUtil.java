package com.app.japub.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String formatDateString(String boardRegisterDate) {
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(boardRegisterDate);
			return new SimpleDateFormat("yy-MM-dd HH:mm").format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return boardRegisterDate;
		}
	}
}
