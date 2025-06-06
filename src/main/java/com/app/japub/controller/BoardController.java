package com.app.japub.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.japub.common.MessageConstants;
import com.app.japub.common.SessionUtil;
import com.app.japub.domain.dto.BoardDto;
import com.app.japub.domain.dto.Criteria;
import com.app.japub.domain.dto.FileDto;
import com.app.japub.domain.dto.PageDto;
import com.app.japub.domain.service.board.BoardService;
import com.app.japub.domain.service.comment.CommentService;
import com.app.japub.domain.service.file.FileService;
import com.app.japub.domain.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final HttpSession session;
	private final BoardService boardService;
	private final CommentService commentService;
	private final UserService userService;
	private final FileService fileService;
	private static final String DEFULT_CATEGORY = "free";
	private static final String NOTICE_CATEGORY = "notice";
	public static final List<String> ADMIN_WRITE_CATEGORIES = List.of("notice", "upload");

	@GetMapping("/list")
	public String list(Criteria criteria, Model model) {
		final String referrer = "main";
		if (criteria.getReferrer() != null && referrer.equals(criteria.getReferrer())) {
			return "redirect:/main";
		}
		String category = criteria.getCategory();
		if (category == null || category.equals("")) {
			criteria.setCategory(DEFULT_CATEGORY);
		}
		List<BoardDto> boards = boardService.findByCriteria(criteria);
		boards.forEach(board -> {
			String boardRegisterDate = formatDateString(board.getBoardRegisterDate());
			board.setBoardRegisterDate(boardRegisterDate);
		});
		if (ADMIN_WRITE_CATEGORIES.contains(criteria.getCategory())) {
			model.addAttribute("writable", false);
		}
		model.addAttribute("boards", boards);
		model.addAttribute("pageDto", new PageDto(criteria, boardService.countByCriteria(criteria)));
		return "board/list";
	}

	@GetMapping("/write")
	public String write(Criteria criteria, Model model, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		
		if (ADMIN_WRITE_CATEGORIES.contains(criteria.getCategory())) {
			if (!(boolean) session.getAttribute(SessionUtil.ADMIN_KEY)) {
				attributes.addFlashAttribute("msg", MessageConstants.ADMIN_NOT_FOUND_MSG);
				return "redirect:/board/list" + criteria.getParams();
			}
		}
		String userId = userService.findByUserNum(userNum).getUserId();
		model.addAttribute("userId", userId);
		return "board/write";
	}

	@PostMapping("/write")
	public String write(BoardDto boardDto, Criteria criteria, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		boardDto.setUserNum(userNum);
		boardDto.setBoardCategory(criteria.getCategory());
		try {
			boardService.insert(boardDto);
			attributes.addAttribute("category", criteria.getCategory());
			return "redirect:/board/list";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			attributes.addFlashAttribute("board", boardDto);
			return "redirect:/board/write" + criteria.getParams();
		}
	}

	@GetMapping("/detail")
	public String detail(Criteria criteria, Long boardNum, RedirectAttributes attributes, Model model) {
		if (boardNum == null) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		BoardDto boardDto = boardService.findByBoardNum(boardNum);
		if (boardDto == null) {
			attributes.addFlashAttribute("msg", MessageConstants.BOARD_NOT_FOUND_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		boardService.incrementBoardReadCount(boardNum);
		boardDto = boardService.findByBoardNum(boardNum);
		boardDto.setBoardCommentCount(commentService.countByBoardNum(boardNum));
		boardDto.setBoardRegisterDate(formatDateString(boardDto.getBoardRegisterDate()));

		model.addAttribute("board", boardDto);
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		if (userNum != null) {
			String userId = userService.findByUserNum(userNum).getUserId();
			model.addAttribute("userId", userId);
		}

		if (criteria.getCategory().equals(NOTICE_CATEGORY)) {
			List<FileDto> files = fileService.findByBoardNum(boardNum);
			model.addAttribute("files", files);
			return "board/notice-detail";
		}

		return "board/detail";
	}

	@GetMapping("/delete")
	public String delete(Criteria criteria, Long boardNum, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		if (boardNum == null) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		if (!boardService.delete(userNum, boardNum)) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			attributes.addAttribute("boardNum", boardNum);
			return "redirect:/board/detail" + criteria.getParams();
		}
		return "redirect:/board/list" + criteria.getParams();
	}

	@GetMapping("/update")
	public String update(Criteria criteria, Long boardNum, RedirectAttributes attributes, Model model) {
		if (session.getAttribute(SessionUtil.KEY) == null) {
			return MessageConstants.LOGIN_URL;
		}
		if (model.getAttribute("board") != null) {
			return "board/update";
		}
		if (boardNum == null) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		BoardDto boardDto = boardService.findByBoardNum(boardNum);
		if (boardDto == null) {
			attributes.addFlashAttribute("msg", MessageConstants.BOARD_NOT_FOUND_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		boardDto.setBoardRegisterDate(formatDateString(boardDto.getBoardRegisterDate()));
		boardDto.setBoardCommentCount(commentService.countByBoardNum(boardNum));
		model.addAttribute("board", boardDto);
		return "board/update";
	}

	@PostMapping("/update")
	public String update(Criteria criteria, BoardDto boardDto, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		boardDto.setUserNum(userNum);
		try {
			boardService.update(boardDto);
			attributes.addAttribute("boardNum", boardDto.getBoardNum());
			return "redirect:/board/detail" + criteria.getParams();
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			attributes.addFlashAttribute("board", boardDto);
			return "redirect:/board/update" + criteria.getParams();
		}
	}

	private String formatDateString(String boardRegisterDate) {
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(boardRegisterDate);
			return new SimpleDateFormat("yy-MM-dd HH:mm").format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return boardRegisterDate;
		}
	}
}
