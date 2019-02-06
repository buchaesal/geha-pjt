package com.bit.geha.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.criteria.SearchCriteria;
import com.bit.geha.dao.SearchDao;
import com.bit.geha.dto.SearchDto;

@Controller
public class SearchController {

	@Autowired
	private SearchDao dao;
	
	@RequestMapping("search")
	public String search(SearchCriteria sc, Model model) {
		model.addAttribute("list", dao.listGeha());
		/*model.addAttribute("list", dao.searchGeha(sc));*/
		return "search";
	}
	
	@GetMapping("allgehainfo")
	@ResponseBody
	public List<SearchDto> searchapi(SearchCriteria sc){
		return dao.listGeha();
	}
	
	@GetMapping("searchgehainfo")
	@ResponseBody
	public List<SearchDto> searchGehaInfo(SearchCriteria sc){
		
		return dao.searchGeha(sc);
	}
	
}
