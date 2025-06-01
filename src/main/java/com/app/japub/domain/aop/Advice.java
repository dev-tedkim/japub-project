package com.app.japub.domain.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Advice {
	
	@Around("execution(* com.example.spring61.domain.aop.*.*(..))")
	public Object arround(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("전처리");
		Object result = joinPoint.proceed();
		System.out.println("후처리");
		return (int)result + 1;
	}
}
