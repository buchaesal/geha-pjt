package com.bit.geha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bit.geha.criteria.AdminPageCriteria;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.MemberDto;
import com.bit.geha.dto.RejectDto;

@Mapper
public interface AdminPageDao {

	
	public List<MemberDto> getMemberList(@Param("cri")AdminPageCriteria cri,String auth);
	
	public void changeAdmin(@Param("id")List<String> id);
	
	public void changeUser(@Param("id")List<String> id);
	
	public int getTotal(AdminPageCriteria cri,String auth);
	
	public List<GuestHouseDto> getApprovalHouseList();
	
	public void approveNewGuestHouse(int guestHouseCode);
	
	public void rejectNewGuestHouse(int guestHouseCode);
	
	public void insertReject(@Param("dto")RejectDto rejectDto);
	
	public List<RejectDto> getRejectListByGuestHouseCode(int guestHouseCode);
	
}
