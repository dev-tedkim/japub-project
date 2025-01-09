package com.example.spring61.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.spring61.domain.dto.BoardDto;
import com.example.spring61.domain.dto.FileDto;
import com.example.spring61.domain.service.board.BoardService;
import com.example.spring61.domain.service.comment.CommentService;

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

	@Test
	public void test() {
		BoardDto boardDto = new BoardDto();
		FileDto fileDto = new FileDto();
		fileDto.setFileName("test파일");
		fileDto.setFileType(true);
		fileDto.setUploadPath("2025/01/01");
		fileDto.setUuid("uuid");
		List<FileDto> files = new ArrayList<>();
		files.add(fileDto);
		boardDto.setFiles(files);
		boardDto.setBoardCategory("free");
		boardDto.setBoardContent("gd");
		boardDto.setBoardTitle("ㅎㅇ");
		boardDto.setUserNum(50l);
		boardService.insert(boardDto);
	}
}



