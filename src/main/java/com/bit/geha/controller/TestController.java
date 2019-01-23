package com.bit.geha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.dao.TestMapper;


@Controller
public class TestController {

	
	@Autowired
	TestMapper testMapper;

	
	@RequestMapping("/")
	public String home(Model model) {

		return "home";
	}
	
	@RequestMapping("/roominfo")
	public void roominfo() {}
	
	@RequestMapping("/test")
	public void test() {}
	

}
