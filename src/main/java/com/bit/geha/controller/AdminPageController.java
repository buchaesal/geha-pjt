package com.bit.geha.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bit.geha.criteria.AdminPageCriteria;
import com.bit.geha.dao.AdminPageDao;
import com.bit.geha.dto.MemberDto;
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
	public String changeAdmin(@RequestParam("id") List<String> id,
			RedirectAttributes redirectAttributes) {
		adminPageDao.changeAdmin(id);
		redirectAttributes.addFlashAttribute("changeAdmin","선택한 회원을 관리자로 임명하였습니다");
		return "redirect:/adminPage/memberList";
	}
	
	@RequestMapping("/changeUser")
	public String changeUser(@RequestParam("id") List<String> id,
			RedirectAttributes redirectAttributes) {
		adminPageDao.changeUser(id);
		redirectAttributes.addFlashAttribute("changeUser","선택한 관리자를 일반회원으로 강등하였습니다.");
		return "redirect:/adminPage/memberList";
	}

	
	@RequestMapping("/approvalHouse")
	public void approvalHouse(Model model) {
		model.addAttribute("list",adminPageDao.getApprovalHouseList());
	}
	
	@RequestMapping("getMemberInfo.do")
	@ResponseBody
	public List<MemberDto> getMemberInfo(@RequestBody int memberCode) {
		
		
		return memberService.findByMemberCode(memberCode);
	}
}
