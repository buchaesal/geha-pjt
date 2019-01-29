package com.bit.geha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/kskRoom")
@Log
public class KskRoomController {
	
	@RequestMapping("/selectRoom")
	public void selectRoom() {
		log.info("selectRoom()");
	}
}
