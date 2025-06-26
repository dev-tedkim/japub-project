package com.app.japub.controller;

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
	private static List<String> CATEGORIES = List.of("free", "upload", "notice");
	private static final String DEFULT_CATEGORY = "free";
	public static final List<String> ADMIN_WRITE_CATEGORIES = List.of("notice", "upload");

	@GetMapping("/list")
	public String list(Criteria criteria, Model model) {
		final String referrer = "main";
		if (criteria.getReferrer() != null && referrer.equals(criteria.getReferrer())) {
			return "redirect:/main";
		}
		if (criteria.getCategory() == null || "".equals(criteria.getCategory())) {
			criteria.setCategory(DEFULT_CATEGORY);
		}
		List<BoardDto> boards = boardService.findByCriteria(criteria);
		boards.forEach(boardService::setBoardRegisterDate);
		if (isAdminWriteCategory(criteria)) {
			model.addAttribute("writable", false);
		}
		model.addAttribute("boards", boards);
		model.addAttribute("pageDto", new PageDto(criteria, boardService.countByCriteria(criteria)));
		return "board/list";
	}

	@GetMapping("/write")
	public String write(Criteria criteria, Model model, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}

		if (!validateCategory(criteria)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.CATEGORY_NOT_FOUND_MSG);
			return "redirect:/board/list";
		}

		if (isAdminWriteCategory(criteria) && !SessionUtil.isAdmin(session)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.CATEGORY_NOT_FOUND_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		String userId = userService.findByUserNum(userNum).getUserId();
		model.addAttribute("userId", userId);
		return "board/write";
	}

	@PostMapping("/write")
	public String write(BoardDto boardDto, Criteria criteria, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		if (!validateCategory(criteria)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.CATEGORY_NOT_FOUND_MSG);
			return "redirect:/board/list";
		}
		if (isAdminWriteCategory(criteria) && !SessionUtil.isAdmin(session)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ADMIN_NOT_FOUND_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		boardDto.setUserNum(userNum);
		boardDto.setBoardCategory(criteria.getCategory());
		try {
			boardService.insert(boardDto);
			attributes.addAttribute("category", criteria.getCategory());
			return "redirect:/board/list";
		} catch (Exception e) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			attributes.addFlashAttribute("board", boardDto);
			return "redirect:/board/write" + criteria.getParams();
		}
	}

	@GetMapping("/detail")
	public String detail(Criteria criteria, Long boardNum, RedirectAttributes attributes, Model model) {
		if (boardNum == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		BoardDto boardDto = boardService.findByBoardNum(boardNum);
		if (boardDto == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.BOARD_NOT_FOUND_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		boardService.incrementBoardReadCount(boardNum);
		boardDto = boardService.findByBoardNum(boardNum);
		boardDto.setBoardCommentCount(commentService.countByBoardNum(boardNum));
		boardService.setBoardRegisterDate(boardDto);
		model.addAttribute("board", boardDto);
		addUserIdToModel(session, model);
		SessionUtil.addIsAdminToModel(model, session);
		return resolveDetailView(criteria, boardNum, model);
	}

	@GetMapping("/delete")
	public String delete(Criteria criteria, Long boardNum, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		if (boardNum == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.BOARD_NOT_FOUND_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		if (!boardService.delete(userNum, boardNum)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			attributes.addAttribute("boardNum", boardNum);
			return "redirect:/board/detail" + criteria.getParams();
		}
		return "redirect:/board/list" + criteria.getParams();
	}

	@GetMapping("/update")
	public String update(Criteria criteria, Long boardNum, RedirectAttributes attributes, Model model) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		if (model.getAttribute("board") != null) {
			return "board/update";
		}
		if (boardNum == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		BoardDto boardDto = boardService.findByUserNumAndBoardNum(userNum, boardNum);
		if (boardDto == null) {
			attributes.addAttribute("boardNum", boardNum);
			String msg = "해당 게시글을 수정할 수 없습니다.";
			attributes.addFlashAttribute("msg", msg);
			return "redirect:/board/list" + criteria.getParams();
		}
		boardService.setBoardRegisterDate(boardDto);
		boardDto.setBoardCommentCount(commentService.countByBoardNum(boardNum));
		model.addAttribute("board", boardDto);
		return "board/update";
	}

	@PostMapping("/update")
	public String update(Criteria criteria, BoardDto boardDto, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		try {
			boardDto.setUserNum(userNum);
			boardService.update(boardDto);
			attributes.addAttribute("boardNum", boardDto.getBoardNum());
			return "redirect:/board/detail" + criteria.getParams();
		} catch (Exception e) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			attributes.addFlashAttribute("board", boardDto);
			attributes.addAttribute("boardNum", boardDto.getBoardNum());
			return "redirect:/board/update";
		}
	}

	private boolean validateCategory(Criteria criteria) {
		if (criteria.getCategory() == null) {
			return false;
		}
		if (!CATEGORIES.contains(criteria.getCategory())) {
			return false;
		}
		return true;
	}

	private boolean isAdminWriteCategory(Criteria criteria) {
		return ADMIN_WRITE_CATEGORIES.contains(criteria.getCategory());
	}

	private void addUserIdToModel(HttpSession session, Model model) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum != null) {
			String userId = userService.findByUserNum(userNum).getUserId();
			model.addAttribute("userId", userId);
		}
	}

	private String resolveDetailView(Criteria criteria, Long boardNum, Model model) {
		if ("notice".equals(criteria.getCategory())) {
			List<FileDto> files = fileService.findByBoardNum(boardNum);
			model.addAttribute("files", files);
			return "board/notice-detail";
		} else {
			return "board/detail";
		}
	}

}
