package com.app.japub.controller;

import java.util.Arrays;
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
import com.app.japub.common.ViewPathUtil;
import com.app.japub.domain.dto.BoardDto;
import com.app.japub.domain.dto.Criteria;
import com.app.japub.domain.dto.FileDto;
import com.app.japub.domain.dto.PageDto;
import com.app.japub.domain.dto.UserDto;
import com.app.japub.domain.service.admin.AdminService;
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
	private final FileService fileService;
	private final AdminService adminService;
	private final UserService userService;
	private static final String DEFAULT_CATEGORY = "free";
	private static final List<String> CATEGORIES = Arrays.asList("free", "notice", "download", "media");
	private static final List<String> ADMIN_CATEGORIES = Arrays.asList("notice", "media", "download");
	private static final String BASE_PATH = "board";
	private static final String LIST_PATH = "list";
	private static final String WRITE_PATH = "write";
	private static final String UPDATE_PATH = "update";
	private static final String DETAIL_PATH = "detail";

	@GetMapping("/list")
	public void list(Criteria criteria, Model model) {
		setCategory(criteria);
		List<BoardDto> boards = boardService.findByCriteria(criteria);
		boards.forEach(boardService::setBoardRegisterDate);
		model.addAttribute("boards", boards);
		model.addAttribute("pageDto", new PageDto(criteria, boardService.countByCriteria(criteria)));
		model.addAttribute("writable", !ADMIN_CATEGORIES.contains(criteria.getCategory()));
	}

	@GetMapping("/write")
	public String write(Criteria criteria, RedirectAttributes attributes, Model model) {
		Long userNum = SessionUtil.getSessionNum(session);
		String result = validateBoardWrite(userNum, criteria, attributes);
		if (result != null) {
			return result;
		}
		addUserIdInModel(model);
		return ViewPathUtil.getForwardPath(BASE_PATH, WRITE_PATH);
	}

	@PostMapping("/write")
	public String write(Criteria criteria, BoardDto boardDto, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		String result = validateBoardWrite(userNum, criteria, attributes);
		if (result != null) {
			return result;
		}
		boardDto.setUserNum(userNum);
		boardDto.setBoardCategory(criteria.getCategory());
		try {
			boardService.insert(boardDto);
			attributes.addAttribute("category", criteria.getCategory());
			return ViewPathUtil.getRedirectPath(null, BASE_PATH, LIST_PATH);
		} catch (Exception e) {
			addBoardInFlash(attributes, boardDto);
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, WRITE_PATH);
		}
	}

	@GetMapping("/update")
	public String update(Criteria criteria, Long boardNum, RedirectAttributes attributes, Model model) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		if (isBoardInModel(model)) {
			return ViewPathUtil.getForwardPath(BASE_PATH, UPDATE_PATH);
		}
		if (boardNum == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		BoardDto boardDto = boardService.findByBoardNum(boardNum);
		if (boardDto == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.BOARD_NOT_FOUND_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		boardDto = boardService.findByUserNumAndBoardNum(userNum, boardNum);
		if (boardDto == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.BOARD_NO_PERMISSION_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		Long commentCount = commentService.countByBoardNum(boardNum);
		boardDto.setBoardCommentCount(commentCount);
		boardService.setBoardRegisterDate(boardDto);
		addBoardInModel(model, boardDto);
		return ViewPathUtil.getForwardPath(BASE_PATH, UPDATE_PATH);
	}

	@PostMapping("/update")
	public String update(Criteria criteria, BoardDto boardDto, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		boardDto.setUserNum(userNum);
		try {
			boardService.update(boardDto);
			addBoardNumParam(attributes, boardDto);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, DETAIL_PATH);
		} catch (Exception e) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			addBoardInFlash(attributes, boardDto);
			addBoardNumParam(attributes, boardDto);
			System.out.println(boardDto);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, UPDATE_PATH);
		}
	}

	@GetMapping("/detail")
	public String detail(Criteria criteria, Long boardNum, RedirectAttributes attributes, Model model) {
		if (boardNum == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		BoardDto boardDto = boardService.findByBoardNum(boardNum);
		if (boardDto == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.BOARD_NOT_FOUND_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		boardService.incrementBoardReadCount(boardNum);
		boardDto = boardService.findByBoardNum(boardNum);
		Long commentCount = commentService.countByBoardNum(boardNum);
		boardDto.setBoardCommentCount(commentCount);
		boardService.setBoardRegisterDate(boardDto);
		addBoardInModel(model, boardDto);
		addUserIdInModel(model);
		addFilesToModel(model, boardDto);
		addUserIdToModel(model);
		SessionUtil.addIsAdminToModel(model, session);
		model.addAttribute("isShowImage", !("download".equals(boardDto.getBoardCategory())));
		return ViewPathUtil.getForwardPath(BASE_PATH, DETAIL_PATH);
	}

	@GetMapping("/delete")
	public String delete(Criteria criteria, Long boardNum, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		if (boardNum == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		if (boardService.findByBoardNum(boardNum) == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.BOARD_NOT_FOUND_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		boolean result = SessionUtil.isAdmin(session) ? adminService.deleteByBoardNum(boardNum)
				: boardService.delete(userNum, boardNum);
		if (!result) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.BOARD_NO_PERMISSION_MSG);
		}
		return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
	}

	private String validateBoardWrite(Long userNum, Criteria criteria, RedirectAttributes attributes) {
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		if (!validateCategory(criteria)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.CATEGORY_NOT_FOUND_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		if (validateAdminCategory(criteria) && !SessionUtil.isAdmin(session)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ADMIN_NOT_FOUND_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		return null;
	}

	private void setCategory(Criteria criteria) {
		String category = criteria.getCategory();
		if (category == null || category.isEmpty()) {
			criteria.setCategory(DEFAULT_CATEGORY);
		}
	}

	private boolean validateCategory(Criteria criteria) {
		return CATEGORIES.contains(criteria.getCategory());
	}

	private boolean validateAdminCategory(Criteria criteria) {
		return ADMIN_CATEGORIES.contains(criteria.getCategory());
	}

	private void addBoardInFlash(RedirectAttributes attributes, BoardDto boardDto) {
		attributes.addFlashAttribute("board", boardDto);
	}

	private void addBoardInModel(Model model, BoardDto boardDto) {
		model.addAttribute("board", boardDto);
	}

	private void addBoardNumParam(RedirectAttributes attributes, BoardDto boardDto) {
		attributes.addAttribute("boardNum", boardDto.getBoardNum());
	}

	private void addUserIdInModel(Model model) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return;
		}
		model.addAttribute("userId", userService.findByUserNum(userNum).getUserId());
	}

//	private void addFilesToModel(Model model, BoardDto boardDto) {
//		boolean result = ADMIN_CATEGORIES.contains(boardDto.getBoardCategory());
//		if (result) {
//			List<FileDto> files = fileService.findByBoardNum(boardDto.getBoardNum());
//			files.forEach(fileService::setFilePath);
//			model.addAttribute("files", files);
//		}
//	}

	private void addFilesToModel(Model model, BoardDto boardDto) {
		List<FileDto> files = fileService.findByBoardNum(boardDto.getBoardNum());
		if (files != null && !files.isEmpty()) {
			files.forEach(fileService::setFilePath);
			model.addAttribute("files", files);
		}
	}

	private void addUserIdToModel(Model model) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return;
		}
		UserDto userDto = userService.findByUserNum(userNum);
		model.addAttribute("userId", userDto == null ? "" : userDto.getUserId());
	}

	private boolean isBoardInModel(Model model) {
		return model.getAttribute("board") != null;
	}

}
