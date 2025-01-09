package com.example.spring61.domain.aop;

import org.springframework.stereotype.Component;

@Component
public class Target {
	public int sum(int a, int b) {
		return a + b;
	}
}
