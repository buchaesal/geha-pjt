package com.bit.geha.controller;



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
		
		
		return "home";
	}
	
	
	
	
}