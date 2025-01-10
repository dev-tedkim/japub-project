package com.example.spring61.domain.service;

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
		System.out.println(fileService.findByBoardNum(4166l));
	}
}