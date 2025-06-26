package com.app.japub.domain.service.user;

import com.app.japub.domain.dto.UserDto;

public interface MailService {
	public abstract boolean sendMail(UserDto userDto);
	public abstract String getMailContent(UserDto userDto);
}
