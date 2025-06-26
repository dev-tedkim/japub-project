package com.app.japub.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.japub.common.MessageConstants;
import com.app.japub.common.SessionUtil;
import com.app.japub.domain.dto.Criteria;
import com.app.japub.domain.service.admin.AdminService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	private final HttpSession session;
	private final AdminService adminService;

	@GetMapping("/board/delete")
	public String boardDelete(Long boardNum, Criteria criteria, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		boolean isAdmin = SessionUtil.isAdmin(session);
		if (userNum == null) {
			return MessageConstants.LOGIN_URL;
		}
		if (!isAdmin) {
			attributes.addFlashAttribute("msg", MessageConstants.ADMIN_NOT_FOUND_MSG);
			attributes.addAttribute("boardNum", boardNum);
			return "redirect:/board/detail" + criteria.getParams();
		}
		if (boardNum == null) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		if (!adminService.deleteByBoardNum(boardNum)) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			attributes.addAttribute("boardNum", boardNum);
			return "redirect:/board/detail" + criteria.getParams();
		}
		return "redirect:/board/list" + criteria.getParams();
	}

}
