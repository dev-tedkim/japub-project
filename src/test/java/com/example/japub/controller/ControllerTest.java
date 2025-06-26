package com.example.japub.controller;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.app.japub.domain.dto.ProductDto;

@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/*.xml")
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ControllerTest {
	@Autowired
	private WebApplicationContext context;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void controllerTest() throws Exception {
		List<ProductDto> products = (List<ProductDto>) mockMvc.perform(MockMvcRequestBuilders.get("/products/list").queryParam("page", "26"))
				.andReturn().getModelAndView().getModelMap().get("products");
		System.out.println(products.size());
	}

}
