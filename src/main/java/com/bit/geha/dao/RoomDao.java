package com.bit.geha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bit.geha.dto.FacilityDto;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.ReplyReviewDto;
import com.bit.geha.dto.RoomDto;

@Mapper
public interface RoomDao {
	
	List<GuestHouseDto> selectGuestHouse();
	
	GuestHouseDto gehaInfo(int guestHouseCode);
	List<RoomDto> roomInfo(int guestHouseCode);
	List<FacilityDto> facilityInfo(int guestHouseCode);

}
