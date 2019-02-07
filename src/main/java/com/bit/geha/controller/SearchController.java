package com.bit.geha.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.criteria.SearchCriteria;
import com.bit.geha.dao.SearchDao;
import com.bit.geha.dto.SearchDto;
import com.bit.geha.service.MemberService;

@Controller
public class SearchController {

	@Autowired
	private SearchDao dao;
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping("/search")
	public String search(Model model,Authentication auth,HttpSession session) {
		memberService.getSession(auth, session);
		model.addAttribute(dao.listGeha());
		return "search";
	}
	
	@GetMapping("/allgehainfo")
	@ResponseBody
	public List<SearchDto> searchapi(SearchCriteria sc){
		return dao.listGeha();
	}
	
	@GetMapping("/searchgehainfo")
	@ResponseBody
	public List<SearchDto> searchGehaInfo(SearchCriteria sc){
		System.out.println("searchGehaInfo()");
		
		if(sc.getGender() == null) {
			List<String> gender = new ArrayList<>();
			sc.setGender(gender);
		}
		System.out.println(sc.getGender());
		System.out.println(sc.getGender().size());
		
		if(sc.getFacilities() == null) {
			List<Integer> facilities = new ArrayList<>();
			sc.setFacilities(facilities);
		}
		System.out.println(sc.getFacilities());
		System.out.println(sc.getFacilities().size());
		
		return dao.searchGeha(sc);
	}
	
}
