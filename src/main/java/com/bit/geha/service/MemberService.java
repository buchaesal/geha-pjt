package com.bit.geha.service;

import com.bit.geha.dto.MemberDto;

public interface MemberService {
	
	public void save(MemberDto memberDto) throws Exception;
	
	public String findPw(String id) throws Exception;
	

}
