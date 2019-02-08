package com.bit.geha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bit.geha.criteria.AdminPageCriteria;
import com.bit.geha.dto.MemberDto;

@Mapper
public interface AdminPageDao {

	
	public List<MemberDto> getMemberList(@Param("cri")AdminPageCriteria cri,String auth);
	
	public void changeAdmin(String id);
	
	public void changeUser(String id);
	
	public int getTotal(AdminPageCriteria cri,String auth);
}
