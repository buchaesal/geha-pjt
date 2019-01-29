package com.bit.geha.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.security.SecurityMember;

@Controller
public class CommonController {

	@RequestMapping("/")
	public String home(Model model, Authentication auth) {
		if (auth instanceof OAuth2Authentication) {
			OAuth2Authentication oauth = (OAuth2Authentication) auth;
			
			/*
			 * SecurityContextHolder .getContext() .getAuthentication();
			 */

			Map<String, String> map = (HashMap<String, String>) oauth
					.getUserAuthentication().getDetails();

			model.addAttribute("name", map.get("name"));
		} else if (auth == null) {
			model.addAttribute("name", null);
		} else {

			SecurityMember sc = (SecurityMember) auth.getPrincipal();
			model.addAttribute("name", sc.getMemberName());

		}

		return "home";
	}


}