package com.bit.geha.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.dao.MemberDao;
import com.bit.geha.dto.MemberDto;

@Controller
public class MemberController {

	@Autowired
	MemberDao memberDao;

	@PostMapping(value = "/idcheck.do")
	@ResponseBody
	public Map<Object, Object> idcheck(@RequestBody String id) {

		int count = 0;
		Map<Object, Object> map = new HashMap<Object, Object>();

		count = memberDao.idCheck(id);
		map.put("cnt", count);

		return map;
	}

	@PostMapping("/create")
	public String create(MemberDto member) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		member.setPassword(passwordEncoder.encode(member.getPassword()));
		member.setAuthority("USER");
		memberDao.insertUser(member);

		return "redirect:/";

	}
}
