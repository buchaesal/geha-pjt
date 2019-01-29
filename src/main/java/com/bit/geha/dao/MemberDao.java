package com.bit.geha.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bit.geha.dto.MemberDto;



@Mapper
public interface MemberDao {

	public void insertUser(@Param("member")MemberDto memberDto);
	
	public MemberDto findById(String id);
	//public void insertUserAutority(String authorityCode, String authority);
	
	public int idCheck(String id);
	
	public void createAuthKey(String id, String authCode);
	
	public void userAuth(String id);
}
