package com.bit.geha.controller;



import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.dao.HomeDao;
import com.bit.geha.service.MemberService;

@Controller
public class CommonController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	HomeDao homedao;
	
	@RequestMapping("/")
	public String home(Authentication auth,HttpSession session, Model model) {
		memberService.getSession(auth, session);
		
		model.addAttribute("listRating", homedao.listGehaRating());
		model.addAttribute("listReview", homedao.listGehaReview());
		
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