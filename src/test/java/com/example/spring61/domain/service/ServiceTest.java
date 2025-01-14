package com.example.spring61.domain.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.spring61.domain.service.board.BoardService;
import com.example.spring61.domain.service.comment.CommentService;
import com.example.spring61.domain.service.file.FileService;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/*-servlet.xml")
@WebAppConfiguration
@Log4j
public class ServiceTest {
	@Autowired
	private ApplicationContext context;

	@Autowired
	private CommentService commentService;
	@Autowired
	private BoardService boardService;
	@Autowired
	private FileService fileService;

	@Test
	public void test() {
		 /*String password = "mypassword";
	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA-256");
	            byte[] hashedBytes = md.digest(password.getBytes());

	            // 바이트 배열을 16진수 문자열로 변환
	            StringBuilder sb = new StringBuilder();
	            for (byte b : hashedBytes) {
	                sb.append(String.format("%02x", b));
	            }
	           String result = sb.toString();
	           System.out.println(result);
	        } catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException(e);
	        }
	       */
	
		String number = "010-3699-4975";
		System.out.println(number.replaceAll("-", ""));
	       
	        
	}
}