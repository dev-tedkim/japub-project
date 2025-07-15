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
	private static final String DOWNLOAD_CATEGORY = "download";
	private static final String BOARD_KEY = "board";

	@GetMapping("/list")
	public void list(Criteria criteria, Model model) {
		setCategory(criteria);
		List<BoardDto> boards = boardService.findByCriteria(criteria);
		boards.forEach(boardService::setBoardRegisterDate);
		model.addAttribute("boards", boards);
		model.addAttribute("pageDto", new PageDto(criteria, boardService.countByCriteria(criteria)));
		model.addAttribute("writable", DEFAULT_CATEGORY.equals(criteria.getCategory()) || SessionUtil.isAdmin(session));
	}

	@GetMapping("/write")
	public String write(Criteria criteria, RedirectAttributes attributes, Model model) {
		Long userNum = SessionUtil.getSessionNum(session);
		String redirectPath = validateWriteAndRedirect(userNum, criteria, attributes);
		if (redirectPath != null) {
			return redirectPath;
		}
		addUserIdToModel(model);
		return ViewPathUtil.getForwardPath(BASE_PATH, WRITE_PATH);
	}

	@PostMapping("/write")
	public String write(Criteria criteria, BoardDto boardDto, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		String redirectPath = validateWriteAndRedirect(userNum, criteria, attributes);
		if (redirectPath != null) {
			return redirectPath;
		}
		try {
			boardDto.setUserNum(userNum);
			boardDto.setBoardCategory(criteria.getCategory());
			boardService.insert(boardDto);
			attributes.addAttribute("category", criteria.getCategory());
			return ViewPathUtil.getRedirectPath(null, BASE_PATH, LIST_PATH);
		} catch (Exception e) {
			addBoardToFlash(attributes, boardDto);
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
		if (getBoardFromModel(model) != null) {
			return ViewPathUtil.getForwardPath(BASE_PATH, UPDATE_PATH);
		}
		String redirectPath = validateBoardNumAndRedirect(criteria, boardNum, attributes);
		if (redirectPath != null) {
			return redirectPath;
		}
		redirectPath = validateBoardAndRedirect(boardNum, criteria, attributes);
		if (redirectPath != null) {
			return redirectPath;
		}
		BoardDto boardDto = boardService.findByUserNumAndBoardNum(userNum, boardNum);
		if (boardDto == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.BOARD_NO_PERMISSION_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		setBoardDisplayDate(boardDto);
		addBoardToModel(model, boardDto);
		return ViewPathUtil.getForwardPath(BASE_PATH, UPDATE_PATH);
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
			addBoardNumParam(attributes, boardDto.getBoardNum());
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, DETAIL_PATH);
		} catch (Exception e) {
			String redirectPath = validateBoardAndRedirect(boardDto.getBoardNum(), criteria, attributes);
			if (redirectPath != null) {
				return redirectPath;
			}
			MessageConstants.addErrorMessage(attributes, MessageConstants.BOARD_NO_PERMISSION_MSG);
			addBoardNumParam(attributes, boardDto.getBoardNum());
			addBoardToFlash(attributes, boardDto);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, UPDATE_PATH);
		}
	}

	@GetMapping("/detail")
	public String detail(Criteria criteria, Long boardNum, RedirectAttributes attributes, Model model) {
		String redirectPath = validateBoardNumAndRedirect(criteria, boardNum, attributes);
		if (redirectPath != null) {
			return redirectPath;
		}
		redirectPath = validateBoardAndRedirect(boardNum, criteria, attributes);
		if (redirectPath != null) {
			return redirectPath;
		}
		boardService.incrementBoardReadCount(boardNum);
		BoardDto boardDto = boardService.findByBoardNum(boardNum);
		setBoardDisplayDate(boardDto);
		addBoardToModel(model, boardDto);
		addUserIdToModel(model);
		SessionUtil.addIsAdminToModel(model, session);
		if (showImage(boardDto.getBoardCategory())) {
			model.addAttribute("showImage", true);
			List<FileDto> files = fileService.findByBoardNum(boardNum);
			files.forEach(fileService::setFilePath);
			model.addAttribute("files", files);
		}
		return ViewPathUtil.getForwardPath(BASE_PATH, DETAIL_PATH);
	}

	@GetMapping("/delete")
	public String delete(Criteria criteria, Long boardNum, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		String redirectPath = validateBoardNumAndRedirect(criteria, boardNum, attributes);
		if (redirectPath != null) {
			return redirectPath;
		}

		redirectPath = validateBoardAndRedirect(boardNum, criteria, attributes);
		if (redirectPath != null) {
			return redirectPath;
		}
		boolean isSuccess = SessionUtil.isAdmin(session) ? adminService.deleteByBoardNum(boardNum)
				: boardService.delete(userNum, boardNum);
		if (!isSuccess) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.BOARD_NO_PERMISSION_MSG);
			addBoardNumParam(attributes, boardNum);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, DETAIL_PATH);
		}
		return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
	}

	private String validateWriteAndRedirect(Long userNum, Criteria criteria, RedirectAttributes attributes) {
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		String category = criteria.getCategory();
		if (!validateCategory(category)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.CATEGORY_NOT_FOUND_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		boolean isAdmin = SessionUtil.isAdmin(session);
		if (validateAdminCategory(category) && !isAdmin) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ADMIN_NOT_ALLOW_MSG);
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

	private boolean validateCategory(String category) {
		return CATEGORIES.contains(category);
	}

	private boolean validateAdminCategory(String category) {
		return ADMIN_CATEGORIES.contains(category);
	}

	private void addBoardToFlash(RedirectAttributes attributes, BoardDto boardDto) {
		attributes.addFlashAttribute("board", boardDto);
	}

	private void addBoardToModel(Model model, BoardDto boardDto) {
		model.addAttribute("board", boardDto);
	}

	private void addBoardNumParam(RedirectAttributes attributes, Long boardNum) {
		attributes.addAttribute("boardNum", boardNum);
	}

	private void addUserIdToModel(Model model) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return;
		}
		model.addAttribute("userId", userService.findByUserNum(userNum).getUserId());
	}

	private BoardDto getBoardFromModel(Model model) {
		return (BoardDto) model.getAttribute(BOARD_KEY);
	}

	private String validateBoardNumAndRedirect(Criteria criteria, Long boardNum, RedirectAttributes attributes) {
		if (boardNum == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		return null;
	}

	private String validateBoardAndRedirect(Long boardNum, Criteria criteria, RedirectAttributes attributes) {
		BoardDto boardDto = boardService.findByBoardNum(boardNum);
		if (boardDto == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.BOARD_NOT_FOUND_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		return null;
	}

	private void setBoardDisplayDate(BoardDto boardDto) {
		boardService.setBoardRegisterDate(boardDto);
		boardDto.setBoardCommentCount(commentService.countByBoardNum(boardDto.getBoardNum()));
	}

	private boolean showImage(String category) {
		return !DOWNLOAD_CATEGORY.equals(category);
	}
}
