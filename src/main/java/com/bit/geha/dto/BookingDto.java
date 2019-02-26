package com.bit.geha.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class BookingDto {
	private int bookingCode; //예약코드
	private int memberCode; //회원코드
	private String booker; //예약자 이름
	private String bookerEmail; //예약자 이메일
	private String bookerContact; //예약자 연락처
	private int bookingNumber; //예약 인원 수
	private int roomCode; //방 코드
	private Date bookingStart; //입실예정일
	private Date bookingEnd; //퇴실예정일
	private String approvalNumber; //승인번호
	private Date paymentDate; //결제일자
	private int paymentAmount; //결제금액
	private String bookingStatus; //예약상태
	private Date refundDate; //환불일자
	private int refundAmount; //환불금액
	
	//추가
	private String guestHouseName;
	private String roomName;
	
	public BookingDto() {
		this.paymentDate = new Date(System.currentTimeMillis());
	}
	
}
