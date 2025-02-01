package com.example.spring61.domain.service.user;

import org.springframework.stereotype.Service;

public interface PasswordService {
	public abstract String encode(String password);

	public abstract boolean matches(String password, String encodedPassword);

	public abstract String createTempPassword();
}
