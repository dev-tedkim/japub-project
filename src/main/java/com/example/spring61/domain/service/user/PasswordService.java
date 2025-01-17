package com.example.spring61.domain.service.user;

public interface PasswordService {
	public abstract String encodePassword(String password);
	
	public abstract boolean matches(String password, String encodedPassword);
	
	public abstract String createTempPassword();
}
