package com.bit.geha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.dao.TestDao;



@Controller
public class TestController {

	
	@Autowired
	TestDao testMapper;

	
	@RequestMapping("/")
	public String home() {
		
		return "home";
	}
	
	@RequestMapping("/roominfo")
	public void roominfo() {}
	
	@RequestMapping("/test")
	public void test() {
		
	}
	
	@RequestMapping("/signup")
	public void signup() {

	}

}
