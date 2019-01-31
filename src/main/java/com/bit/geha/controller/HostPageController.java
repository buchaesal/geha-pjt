package com.bit.geha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.dao.HostPageDao;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.RejectDto;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/hostPage")
@Log
public class HostPageController {
	@Autowired
	HostPageDao hostPageDao;
	
	@RequestMapping("/myGuestHouseList")
	public void myGuestHouseList(int memberCode, Model model) {
		log.info("myGuestHouseList()");
		
		List<RejectDto> rejectList = hostPageDao.getRejectList(memberCode);
		System.out.println("rejectList.size() : " + rejectList.size());
		
		List<GuestHouseDto> guestHouseList = hostPageDao.getGuestHouseList(memberCode);
		
		model.addAttribute("guestHouseList", guestHouseList);
		model.addAttribute("rejectList", rejectList);
	}
}
