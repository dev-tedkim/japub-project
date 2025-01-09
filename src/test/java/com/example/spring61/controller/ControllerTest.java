package com.example.spring61.controller;

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

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Log4j
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/*-servlet.xml")
public class ControllerTest {
	@Autowired
	private WebApplicationContext context;
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void test() throws Exception {
		System.out.println(mockMvc.perform(MockMvcRequestBuilders.get("/board/list").param("type", "T").param("keyword", "가입")).andReturn().getModelAndView().getViewName());
//		System.out.println(mockMvc.perform(MockMvcRequestBuilders.get("/board/list").param("type", "T").param("keyword", "가입")).andReturn().getModelAndView().getModelMap());
	}
	
}
