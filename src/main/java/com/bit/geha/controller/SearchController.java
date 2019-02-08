package com.bit.geha.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.criteria.SearchCriteria;
import com.bit.geha.dao.SearchDao;
import com.bit.geha.dto.SearchDto;
import com.bit.geha.service.MemberService;

@Controller
public class SearchController {

	Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	private SearchDao dao;
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping("/search")
	public String search(Model model) {
		logger.info("search()");
		return "search";
	}
	
	@GetMapping("/allgehainfo")
	@ResponseBody
	public List<SearchDto> searchapi(SearchCriteria sc){
		logger.info("/allgehainfo()");
		return dao.listGeha();
	}
	
	@PostMapping(path = "/searchgehainfo", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public List<SearchDto> searchGehaInfo(@RequestBody SearchCriteria sc){
		logger.info("/searchGehaInfo()");
		
		// null exception for gender and facilities
		if(sc.getGender() == null) {
			List<String> gender = new ArrayList<>();
			sc.setGender(gender);
		}
		
		if(sc.getFacilities() == null) {
			List<Integer> facilities = new ArrayList<>();
			sc.setFacilities(facilities);
		}
		
		System.out.println(sc.getKeyword());
		System.out.println(sc.getGender());
		System.out.println(sc.getFacilities());
		System.out.println(sc.getMinprice());
		System.out.println(sc.getMaxprice());
		
		return dao.searchGeha(sc);
	}
	
}
