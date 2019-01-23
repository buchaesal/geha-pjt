package com.bit.geha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonController {	
	
	@RequestMapping("/")
	public String home(Model model) {

		return "home";
	}

}
