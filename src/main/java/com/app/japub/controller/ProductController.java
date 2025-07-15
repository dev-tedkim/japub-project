package com.app.japub.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.japub.common.DateUtil;
import com.app.japub.common.MessageConstants;
import com.app.japub.common.SessionUtil;
import com.app.japub.domain.dto.Criteria;
import com.app.japub.domain.dto.PageDto;
import com.app.japub.domain.dto.ProductDto;
import com.app.japub.domain.service.file.FileService;
import com.app.japub.domain.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;
	private final FileService fileService;
	private final HttpSession session;
	private static final String DEFAULT_DERECTORY = "C:/upload/products";
	private static final int DEFAULT_AMOUNT = 30;
	private static final int MAX_RECOMMEND_SIZE = 8;
	private static final String MAX_RECOMMEND_MSG = "추천 도서는 최대 8개까지만 설정할 수 있습니다.";

	private String isAdminRedirect(RedirectAttributes attributes, HttpSession session) {
		if (!SessionUtil.isAdmin(session)) {
			attributes.addFlashAttribute("msg", MessageConstants.ADMIN_NOT_ALLOW_MSG);
			return "redirect:/main";
		}
		return null; // 관리자일 경우 null 반환
	}

	@GetMapping("/list")
	public String list(Criteria criteria, Model model) {
		criteria.setAmount(DEFAULT_AMOUNT);
		List<ProductDto> products = productService.findByCriteria(criteria);
		products.forEach(product -> {
			product.setProductDiscountPrice(productService.getProductDiscountPrice(product));
			product.setProductThumbnailUrl(productService.getProductThumbnailUrl(product));
		});
		model.addAttribute("products", products);
		model.addAttribute("pageDto", new PageDto(criteria, productService.countByCriteria(criteria)));
		return "products/list";
	}

	@GetMapping("/register")
	public String register(Criteria criteria, RedirectAttributes attributes) {
		if (SessionUtil.isAdmin(session)) {
			return "products/register";
		}
		attributes.addFlashAttribute("msg", MessageConstants.ADMIN_NOT_ALLOW_MSG);
		return "redirect:/main";
	}

	@PostMapping("/register")
	public String register(ProductDto productDto, MultipartFile multipartFile, Criteria criteria,
			RedirectAttributes attributes) {
		String redirect = isAdminRedirect(attributes, session);
		if (redirect != null) {
			return redirect; // 관리자 아니면 리다이렉트
		}
		String datePath = DateUtil.getDatePath();
		File uploadPath = fileService.getUploadPath(DEFAULT_DERECTORY, datePath);
		if (!productService.insert(multipartFile, productDto, uploadPath, datePath)) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			attributes.addFlashAttribute("product", productDto);
			return "redirect:/products/register" + criteria.getProductParams();
		}
		return "redirect:/products/list";

	}

	@GetMapping("/update")
	public String update(Criteria criteria, Long productNum, Model model, RedirectAttributes attributes) {
		String redirect = isAdminRedirect(attributes, session);
		if (redirect != null) {
			return redirect; // 관리자 아니면 리다이렉트
		}
		if (productNum == null) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			return "redirect:/products/list" + criteria.getProductParams();
		}
		ProductDto productDto = productService.findByProductNum(productNum);
		productDto.setProductThumbnailUrl(productService.getProductThumbnailUrl(productDto));
		model.addAttribute("product", productDto);
		return "products/update";
	}

	@PostMapping("/update")
	public String update(MultipartFile multipartFile, Criteria criteria, ProductDto productDto,
			RedirectAttributes attributes) {
		String redirect = isAdminRedirect(attributes, session);
		if (redirect != null) {
			return redirect; // 관리자 아니면 리다이렉트
		}
		String datePath = DateUtil.getDatePath();
		File uploadPath = fileService.getUploadPath(DEFAULT_DERECTORY, datePath);
		if (!productService.update(multipartFile, productDto, uploadPath, datePath)) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			attributes.addAttribute("productNum", productDto.getProductNum());
			return "redirect:/products/update" + criteria.getProductParams();
		}
		return "redirect:/products/list" + criteria.getProductParams();
	}

	@PostMapping("/recommend/add")
	public String recommend(Criteria criteria, Long productNum, RedirectAttributes attributes) {
		String redirect = isAdminRedirect(attributes, session);
		if (redirect != null) {
			return redirect; // 관리자 아니면 리다이렉트
		}
		int recommendSize = productService.findByProductIsRecommend(true).size();
		if (recommendSize == MAX_RECOMMEND_SIZE) {
			attributes.addFlashAttribute("msg", MAX_RECOMMEND_MSG);
		} else {
			productService.updateProductIsRecommend(productNum, true);
			attributes.addFlashAttribute("msg", MessageConstants.SUCCESS_MSG);
		}
		return "redirect:/products/list" + criteria.getProductParams();
	}

	@PostMapping("/recommend/cancel")
	public String recommendCancel(Criteria criteria, Long productNum, RedirectAttributes attributes) {
		String redirect = isAdminRedirect(attributes, session);
		if (redirect != null) {
			return redirect; // 관리자 아니면 리다이렉트
		}
		if (!productService.updateProductIsRecommend(productNum, false)) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
		}
		return "redirect:/main";
	}

	@PostMapping("/delete")
	public String delete(Criteria criteria, Long productNum, RedirectAttributes attributes) {
		String redirect = isAdminRedirect(attributes, session);
		if (redirect != null) {
			return redirect; // 관리자 아니면 리다이렉트
		}
		if (!productService.deleteByProductNum(productNum)) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
		}
		return "redirect:/products/list" + criteria.getProductParams();
	}
}
