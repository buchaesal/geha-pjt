package com.bit.geha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/room")
@Log
public class RoomController {
	
	@RequestMapping("/selectRoom")
	public void selectRoom() {
		log.info("selectRoom()");
	}
}
