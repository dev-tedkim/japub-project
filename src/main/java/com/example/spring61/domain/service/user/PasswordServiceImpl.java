package com.example.spring61.domain.service.user;

import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService{
	private final BCryptPasswordEncoder passwordEncoder;

	public String encode(String password) {
		return passwordEncoder.encode(password);
	}

	public boolean matches(String password, String encodedPassword) {
		return passwordEncoder.matches(password, encodedPassword);
	}

	public String createTempPassword() {
		return new Random().ints(6, 0 , 9).mapToObj(Integer::toString).collect(Collectors.joining());
	}
}
