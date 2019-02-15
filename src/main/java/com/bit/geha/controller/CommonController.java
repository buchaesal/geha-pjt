package com.bit.geha.controller;



import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.service.MemberService;

@Controller
public class CommonController {
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping("/")
	public String home(Authentication auth,HttpSession session) {
		
		memberService.getSession(auth, session);
		
		String path = session.getServletContext().getRealPath("static/gehaImg/");
		System.out.println("path: " + new File(path).exists() + ", " + path);
		session.setAttribute("uploadPath", path);
		
		return "home";
	}
	
	@RequestMapping("/faq")
	public void faq(Authentication auth,HttpSession session) {
		memberService.getSession(auth, session);
	}
	
	@RequestMapping("/kakaoTest")
	public void kakaoTest() {
		
	}
	
	
	
	
	
}