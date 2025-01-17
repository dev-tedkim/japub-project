package com.example.spring61.domain.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.spring61.domain.dto.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

	@Autowired
	private final JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	private String fromEmail = "japubCompany@gmail.com";
	private final String TITLE = "안녕하세요 중앙경제평론사 입니다.";

	@Override
	@Async
	public boolean sendEmail(UserDto userDto) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail); // 보내는 사람 이메일
		message.setTo(userDto.getUserEmail()); // 받는 사람 이메일
		message.setSubject(TITLE); // 이메일 제목
		message.setText(createEmailContent(userDto)); // 이메일 본문
		try {
			mailSender.send(message);
		} catch (MailException e) {
			e.printStackTrace();
			System.out.println("mailService sendEmail error");
			return false;
		}
		return true;
	}

	private String createEmailContent(UserDto userDto) {
		return "귀하의 아이디는 " + userDto.getUserId() + "이고 임시 비밀번호는 " + userDto.getUserPassword()
				+ "입니다. 로그인 후 반드시 비밀번호를 변경하시길 바랍니다.";
	}

}
