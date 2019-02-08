package com.bit.geha.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.bit.geha.dto.FileDto;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.RejectDto;
import com.bit.geha.dto.RoomDto;

@Mapper
public interface HostPageDao {
	
	@Select("SELECT * FROM guestHouse_tb WHERE memberCode=#{memberCode}")
	public List<GuestHouseDto> getGuestHouseList(int memberCode);
	
	@Select("SELECT guestHouseCode FROM reject_tb WHERE memberCode=#{memberCode}")
	public List<Integer> getIsRejectList(int memberCode);
	
	@Select("SELECT * FROM reject_tb WHERE guestHouseCode=#{guestHouseCode}")
	public List<RejectDto> getRejectListByGuestHouseCode(int guestHouseCode);
	
	public void addGuestHouse(GuestHouseDto guestHouseDto);
	
	public void addRooms(List<RoomDto> roomDtoList);
	
	public void addFacilities(Map<String, Object> facilityMap);
	
	public void addFiles(List<FileDto> files);
}
