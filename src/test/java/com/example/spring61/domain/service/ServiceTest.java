package com.example.spring61.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.spring61.domain.service.user.PasswordServiceImpl;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ 
		"file:src/main/webapp/WEB-INF/spring/root-servlet.xml",
	    "file:src/main/webapp/WEB-INF/spring/security-context.xml",
	    "file:src/main/webapp/WEB-INF/spring/email-context.xml"
	})
@WebAppConfiguration
@Log4j
public class ServiceTest {
	@Autowired
	private PasswordServiceImpl passwordService;
	


	@Test
	public void test() {
		System.out.println(passwordService.createTempPassword());
	}
}