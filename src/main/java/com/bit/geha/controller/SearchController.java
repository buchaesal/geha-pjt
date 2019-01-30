package com.bit.geha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.criteria.SearchCriteria;
import com.bit.geha.dao.SearchDao;

@Controller
public class SearchController {

	@Autowired
	private SearchDao dao;
	
	/*@RequestMapping("search")
	public String search(SearchCriteria sc, Model model) {
		
		model.addAttribute("list", dao.listGeha(sc));
		
		return "search";
	}*/
	
	@RequestMapping("search")
	public String search() {
		return "search";
	}
	
}
