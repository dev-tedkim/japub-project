package com.app.japub.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

	public static String getDatePath() {
		return new SimpleDateFormat("yyyy/MM/dd").format(new Date()).toString();
	}

	public static String getYesterDayPath() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()).toString();
	}
}
