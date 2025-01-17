package com.example.spring61.domain.service.user;

import com.example.spring61.domain.dto.UserDto;

public interface MailService {
	public abstract boolean sendEmail(UserDto userDto);
	
}
