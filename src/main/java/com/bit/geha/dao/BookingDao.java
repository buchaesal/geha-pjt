package com.bit.geha.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.bit.geha.dto.BookingDto;
import com.bit.geha.dto.RoomDto;

@Mapper
public interface BookingDao {
	@Select("SELECT * FROM booking_tb WHERE bookingCode=#{bookingCode}")
	public BookingDto getBooking(int bookingCode);
	
	@Select("SELECT * FROM room_tb WHERE roomCode=#{roomCode}")
	public RoomDto getRoom(int roomCode);
	
	@Select("SELECT guestHouseName FROM guestHouse_tb WHERE guestHouseCode=#{guestHouseCode}")
	public String getGuestHouseNameByGuestHouseCode(int guestHouseCode);
	
	@Select("SELECT guestHouseName FROM bkrmghmatching_view WHERE bookingCode=#{bookingCode}")
	public String getGuestHouseNameByBookingCode(int bookingCode);
	
	@Select("SELECT roomName FROM bkrmghmatching_view WHERE bookingCode=#{bookingCode}")
	public String getRoomNameByBookingCode(int bookingCode);
	
	public void addBooking(BookingDto bookingDto);
}
