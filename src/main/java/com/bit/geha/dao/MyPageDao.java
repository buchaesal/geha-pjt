package com.bit.geha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bit.geha.dto.BookingDto;

@Mapper
public interface MyPageDao {
	
	@Select("SELECT b.*, v.guestHouseName, v.roomName FROM booking_tb b, bkrmghmatching_view v "
			+ "WHERE b.memberCode = #{memberCode} AND b.bookingCode=v.bookingCode")
	public List<BookingDto> getBookingListByMemberCode(int memberCode);
	
	@Update("UPDATE booking_tb SET bookingStatus=#{bookingStatus} WHERE bookingCode=#{bookingCode}")
	public void modifyBookingStatus(int bookingCode, String bookingStatus);
	
	@Select("SELECT b.bookingCode FROM booking_tb b, review_tb r "
			+ "WHERE b.bookingStatus = '숙박완료' AND b.memberCode=#{memberCode} AND b.bookingCode=r.bookingCode")
	public List<Integer> getReviewListByMemberCode(int memberCode);
	
	@Select("SELECT memberName FROM member_tb WHERE memberCode=#{memberCode}")
	public String getMemberName(int memberCode);
}
