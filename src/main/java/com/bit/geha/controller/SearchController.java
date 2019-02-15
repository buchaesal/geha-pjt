package com.bit.geha.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	public String search(SearchCriteria sc, Model model) {
		logger.info("search()");
		return "/search";
	}
	
	@PostMapping(path = "/searchgehainfo", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public List<SearchDto> searchGehaInfo(@RequestBody SearchCriteria sc){
		logger.info("/searchGehaInfo()");
		
		// 검색 기본 날짜 설정 (맨 처음 로딩시 빈문자열 처리)
		if(sc.getBookingStart().isEmpty() || sc.getBookingEnd().isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			String strToday = sdf.format(cal.getTime());

			cal.add(Calendar.DATE, 1);
			String strTomorrow = sdf.format(cal.getTime());

			sc.setBookingStart(strToday);
			sc.setBookingStart(strTomorrow);
		}
		
		// null exception for gender and facilities
		if(sc.getGender() == null) {
			List<String> gender = new ArrayList<>();
			sc.setGender(gender);
		}
		if(sc.getFacilities() == null) {
			List<Integer> facilities = new ArrayList<>();
			sc.setFacilities(facilities);
		}
		
		logger.info(sc.getSort());
		
		return dao.searchGeha(sc);
	}
	
}
