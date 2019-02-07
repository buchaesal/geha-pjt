package com.bit.geha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit.geha.dao.AdminPageDao;

@Controller
@RequestMapping("/adminPage")
public class AdminPageController {

	@Autowired
	AdminPageDao adminPageDao;
	
	@RequestMapping("/memberList")
	public void memberList(Model model) {
		model.addAttribute("list",adminPageDao.getMemberList());
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
}
