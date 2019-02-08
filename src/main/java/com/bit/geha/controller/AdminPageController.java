package com.bit.geha.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.criteria.AdminPageCriteria;
import com.bit.geha.dao.AdminPageDao;
import com.bit.geha.service.MemberService;
import com.bit.geha.util.PageMaker;

@Controller
@RequestMapping("/adminPage")
public class AdminPageController {

	@Autowired
	AdminPageDao adminPageDao;
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping("/adminPage")
	public void adminPage() {
	}
	
	@GetMapping("/adminPage")
	public void select(@ModelAttribute("cri") AdminPageCriteria cri,String auth,
			Model model) {
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		
		if(!auth.equals("all")) {
			model.addAttribute("list",adminPageDao.getMemberList(cri,auth));
			model.addAttribute("auth",auth);
			pageMaker.setTotalCount(adminPageDao.getTotal(cri,auth));
		}else {
		model.addAttribute("list",adminPageDao.getMemberList(cri,""));
		model.addAttribute("auth","");
		pageMaker.setTotalCount(adminPageDao.getTotal(cri,""));
		}
		
		
		model.addAttribute("pageMaker", pageMaker);
		
	}
	
	@GetMapping("/memberList")
	public String memberList(@ModelAttribute("cri") AdminPageCriteria cri,Model model,Authentication auth,HttpSession session) {
		memberService.getSession(auth, session);
		
		model.addAttribute("list",adminPageDao.getMemberList(cri,""));
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(adminPageDao.getTotal(cri,""));
		
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("auth","");
		return "/adminPage/adminPage";
	}
	
	@RequestMapping("/changeAdmin")
	public String changeAdmin(@RequestParam("id") String id) {
		adminPageDao.changeAdmin(id);
		return "redirect:/adminPage/memberList";
	}
	
	@RequestMapping("/changeUser")
	public String changeUser(@RequestParam("id") String id) {
		adminPageDao.changeUser(id);
		return "redirect:/adminPage/memberList";
	}
	
	@PostMapping("/authSelect")
	@ResponseBody
	public String authSelect(String param) {
		System.out.println(param);
		
		return "/adminPage/adminPage?auth="+param;
	}
}
