package com.bit.geha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bit.geha.dto.MemberDto;

@Mapper
public interface AdminPageDao {

	
	public List<MemberDto> getMemberList();
	
	public void changeAdmin(String id);
	
	public void changeUser(String id);
}
