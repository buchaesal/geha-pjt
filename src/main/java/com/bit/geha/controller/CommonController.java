package com.bit.geha.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.security.SecurityMember;

@Controller
public class CommonController {

	@RequestMapping("/")
	public String home(Model model, Authentication auth, HttpSession session) {
		if (auth instanceof OAuth2Authentication) { //SNS로그인 사용자
			OAuth2Authentication oauth = (OAuth2Authentication) auth;

			Map<String, String> map = (HashMap<String, String>) oauth
					.getUserAuthentication().getDetails();

			session.setAttribute("name", map.get("name"));
			session.setAttribute("auth", "USER");

		} else if (auth == null) {

		} else { //일반 로그인 사용자

			SecurityMember sc = (SecurityMember) auth.getPrincipal();
			session.setAttribute("name", sc.getMemberName());
			session.setAttribute("auth", sc.getAuthority());

		}

		return "home";
	}

}
