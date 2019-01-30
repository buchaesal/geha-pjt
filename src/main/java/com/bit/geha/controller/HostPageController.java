package com.bit.geha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/hostPage")
@Log
public class HostPageController {
	
	@RequestMapping("/myGuestHouseList")
	public void myGuestHouseList() {
		log.info("myGuestHouseList");
		
		
	}
}
