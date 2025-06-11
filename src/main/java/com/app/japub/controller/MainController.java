package com.app.japub.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.japub.domain.dto.BoardDto;
import com.app.japub.domain.dto.Criteria;
import com.app.japub.domain.dto.ProductDto;
import com.app.japub.domain.service.board.BoardService;
import com.app.japub.domain.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final BoardService boardService;
	private final ProductService productService;

	@GetMapping("/main")
	public void main(Model model) {
		List<ProductDto> recommendProducts = productService.findByProductIsRecommend(true);
		recommendProducts.forEach(product -> product.setProductThumbnailUrl(productService.getProductThumbnailUrl(product)));
		model.addAttribute("recommendProducts", recommendProducts);

		Criteria criteria = new Criteria();
		criteria.setCategory("");
		criteria.setAmount(8);
		List<ProductDto> newProducts = productService.findByCriteria(criteria);
		newProducts.forEach(product -> product.setProductThumbnailUrl(productService.getProductThumbnailUrl(product)));
		model.addAttribute("newProducts", newProducts);

		criteria = new Criteria();
		criteria.setCategory("notice");
		List<BoardDto> noticeBoards = formatBoardDates(boardService.findByCriteria(criteria));
		criteria.setCategory("upload");
		List<BoardDto> uploadBoards = formatBoardDates(boardService.findByCriteria(criteria));
		model.addAttribute("noticeBoards", noticeBoards);
		model.addAttribute("uploadBoards", uploadBoards);
	}

	private String formatDateString(String registerDate) {
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(registerDate);
			return new SimpleDateFormat("yy.MM.dd").format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return registerDate;
		}
	}

	private List<BoardDto> formatBoardDates(List<BoardDto> boards) {
		boards.forEach(board -> {
			board.setBoardRegisterDate(formatDateString(board.getBoardRegisterDate()));
		});
		return boards;
	}

	@GetMapping("/test")
	public String maintest() {
		throw new NullPointerException("npe");
	}

}
