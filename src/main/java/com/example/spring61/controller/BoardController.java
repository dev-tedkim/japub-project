package com.example.spring61.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring61.domain.dto.BoardDto;
import com.example.spring61.domain.dto.Criteria;
import com.example.spring61.domain.dto.PageDto;
import com.example.spring61.domain.dto.UserDto;
import com.example.spring61.domain.service.board.BoardService;
import com.example.spring61.domain.service.comment.CommentService;
import com.example.spring61.domain.service.file.FileService;
import com.example.spring61.domain.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	@Autowired
	private final BoardService boardService;
	@Autowired
	private final CommentService commentService;
	@Autowired
	private final UserService userService;
	@Autowired
	private final FileService fileService;
	private final String ERROR_MSG = "요청처리중 문제가 발생했습니다. 다시 시도해 주세요";
	private final Long ADMIN_NUM = 147l;

	private void setAdminModel(HttpSession session, Model model) {
		Long userNum = (Long) session.getAttribute("userNum");
		if (ADMIN_NUM.equals(userNum)) {
			model.addAttribute("adminNum", ADMIN_NUM);
		}
	}

	@GetMapping("/list")
	public void list(Criteria criteria, Model model, HttpServletRequest req, HttpSession session) {
		setAdminModel(session, model);
		model.addAttribute("boards", boardService.findAllByCriteria(criteria));
		model.addAttribute("pageDto", new PageDto(criteria, boardService.getTotal(criteria)));
	}

	@GetMapping("/detail")
	public String detail(Criteria criteria, Long boardNum, RedirectAttributes attributes, Model model,
			HttpSession session) {
		BoardDto board = boardService.findByBoardNum(boardNum);
		if (board == null) {
			attributes.addFlashAttribute("msg", ERROR_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		Long userNum = (Long) session.getAttribute("userNum");
		UserDto userDto = userService.findByUserNum(userNum);
		String currentUserId = userDto == null ? "" : userDto.getUserId();
		boardService.updateBoardHitByBoardNum(boardNum);
		board = boardService.findByBoardNum(boardNum);
		int commentCount = commentService.getTotalByBoardNum(boardNum);
		model.addAttribute("board", board);
		model.addAttribute("commentCount", commentCount);
		model.addAttribute("userId", currentUserId);
		return "board/detail";
	}

	@GetMapping("/delete")
	public String delete(Criteria criteria, Long boardNum, HttpSession session, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute("userNum");
		if (!boardService.delete(boardNum, userNum)) {
			attributes.addFlashAttribute("msg", ERROR_MSG);
			return "redirect:/board/detail" + criteria.getParams() + "&boardNum=" + boardNum;
		}
		attributes.addAttribute("category", criteria.getCategory());
		return "redirect:/board/list";
	}

	@GetMapping("/write")
	public String write(HttpSession session, Criteria criteria, Model model) {
		if (session.getAttribute("userNum") == null) {
			return "redirect:/user/login";
		}
		setAdminModel(session, model);
		return "board/write";
	}

	@PostMapping("/write")
	public String write(BoardDto boardDto, RedirectAttributes attributes, Criteria criteria, HttpSession session) {
		Long userNum = (Long) session.getAttribute("userNum");
		boardDto.setUserNum(userNum);
		boardDto.setBoardCategory(criteria.getCategory());
		try {
			if (boardService.insert(boardDto)) {
				attributes.addAttribute("category", criteria.getCategory());
				return "redirect:/board/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		attributes.addFlashAttribute("msg", ERROR_MSG);
		attributes.addFlashAttribute("board", boardDto);
		return "redirect:/board/write" + criteria.getParams();
	}

	@GetMapping("/update")
	public String update(Criteria criteria, Long boardNum, RedirectAttributes attributes, Model model, HttpSession session) {
		BoardDto board = boardService.findByBoardNum(boardNum);
		if (board == null) {
			attributes.addFlashAttribute("msg", ERROR_MSG);
			return "redirect:/board/detail" + criteria.getParams() + "&boardNum=" + boardNum;
		}
		if (model.getAttribute("board") == null) {
			model.addAttribute("board", board);
		}
		setAdminModel(session, model);
		return "board/update";
	}

	@PostMapping("/update")
	public String update(BoardDto boardDto, Criteria criteria, RedirectAttributes attributes, HttpSession session) {
		Long userNum = (Long) session.getAttribute("userNum");
		boardDto.setUserNum(userNum);
		boardDto.setBoardCategory(criteria.getCategory());
		if (!boardService.update(boardDto)) {
			attributes.addFlashAttribute("msg", ERROR_MSG);
			attributes.addFlashAttribute("board", boardDto);
			return "redirect:/board/update" + criteria.getParams() + "&boardNum=" + boardDto.getBoardNum();
		}
		return "redirect:/board/detail" + criteria.getParams() + "&boardNum=" + boardDto.getBoardNum();
	}

}
