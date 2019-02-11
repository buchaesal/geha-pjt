package com.bit.geha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bit.geha.criteria.AdminPageCriteria;
import com.bit.geha.dto.MemberDto;

@Mapper
public interface AdminPageDao {

	
	public List<MemberDto> getMemberList(@Param("cri")AdminPageCriteria cri,String auth);
	
	public void changeAdmin(@Param("id")List<String> id);
	
	public void changeUser(@Param("id")List<String> id);
	
	public int getTotal(AdminPageCriteria cri,String auth);
}
