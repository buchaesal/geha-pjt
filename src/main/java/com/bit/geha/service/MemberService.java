package com.bit.geha.service;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;

import com.bit.geha.dto.MemberDto;

public interface MemberService {
	
	public void save(MemberDto memberDto) throws Exception;
	
	public String findPw(String id) throws Exception;
	
	public void getSession(Authentication auth,HttpSession session);
	

}
