package com.bit.geha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RoomController {

	
	@RequestMapping("/room")
	public String roomInfo() {
		
		return "roomInfo";
	}
}
