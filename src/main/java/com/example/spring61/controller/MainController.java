package com.example.spring61.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.spring61.domain.dto.BoardDto;
import com.example.spring61.domain.dto.Criteria;
import com.example.spring61.domain.service.board.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	@Autowired
	private BoardService boardService;

	@GetMapping("/main")
	public void main(Model model) {
		Criteria noticeCriteria = new Criteria();
		Criteria uploadCriteria = new Criteria();
		noticeCriteria.setCategory("notice");
		uploadCriteria.setCategory("upload");
		List<BoardDto> noticeBoards = boardService.findAllByCriteria(noticeCriteria);
		List<BoardDto> uploadBoards = boardService.findAllByCriteria(uploadCriteria);
		model.addAttribute("noticeBoards", noticeBoards);
		model.addAttribute("uploadBoards", uploadBoards);
		model.addAttribute("noticeCriteria", noticeCriteria);
		model.addAttribute("uploadCriteria", uploadCriteria);
	}
}
