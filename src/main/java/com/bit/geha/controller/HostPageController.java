package com.bit.geha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hostPage")
public class HostPageController {
	
	@RequestMapping("/myGuestHouseList")
	public void myGuestHouseList(int memberCode) {
		
	}
}
