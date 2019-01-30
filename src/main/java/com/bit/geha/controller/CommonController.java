package com.bit.geha.controller;



import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.security.SecurityMember;
import com.bit.geha.util.loginGetSession;

@Controller
public class CommonController {
	
	
	@RequestMapping("/")
	public String home(Authentication auth,HttpSession session) {
		
		if(session.getAttribute("name")==null) {
		loginGetSession.getSession(auth, session);
		}
		return "home";
	}
	
	
	
	
}