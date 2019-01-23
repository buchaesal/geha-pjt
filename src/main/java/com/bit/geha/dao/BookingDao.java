package com.bit.geha.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BookingDao {
	/*
	@Select("select * from room_tb where roomCode=#{roomCode}")
	public RoomDto getRoom(int roomCode	);
	*/
	@Select("select guestHouseName from guestHouse_tb where guestHouseCode=#{guestHouseCode}")
	public String getGuestHouseName(int guestHouseCode);
}
