package com.app.japub.domain.service.user;

public interface PasswordService {
	public abstract String encode(String rawPassword);
	public abstract boolean matches(String rawPassword, String encodedPassword);
	public abstract String getTempPassword();
}
