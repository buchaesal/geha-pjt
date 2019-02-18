package com.bit.geha.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;

import com.bit.geha.dto.MemberDto;

public interface MemberService {
	
	public void save(MemberDto memberDto) throws Exception;
	
	public String findPw(String id) throws Exception;
	
	public void getSession(Authentication auth,HttpSession session);
	
	public void sendMail(String id) throws Exception;
	
	public List<MemberDto> findByMemberCode(int memberCode);
	
	public String getTomorrow();

}
