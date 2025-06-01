package com.example.japub.domain.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.app.japub.domain.service.admin.AdminService;
import com.app.japub.domain.service.board.BoardService;
import com.app.japub.domain.service.comment.CommentService;
import com.app.japub.domain.service.file.FileService;
import com.app.japub.domain.service.product.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/*.xml")
@WebAppConfiguration
public class ServiceTest {
	@Autowired
	private FileService fileService;
	@Autowired
	private ProductService productService;
	@Autowired
	private BoardService boardService;
	@Autowired
	private AdminService adminService;
	private static final String DOWNLOAD_DERECTORY = "C:/upload/download";
	private static final String PRODUCTS_DERECTORY = "C:/upload/products";
	
	@Test
	public void test() {
		System.out.println(adminService.deleteByCommentNum(83l));
	}

	private String getDatePath() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());
	}

}