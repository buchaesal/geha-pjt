package com.bit.geha.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import lombok.Data;

@Data
public class ReviewDto {
	private int reviewNo;
	private int bookingCode;
	private double rating;
	private Timestamp writingDate;
	private String title;
	private String content;
	private String replyContent;
	private Timestamp replyDate;
	
	//추가
	private String guestHouseName;
	private String roomName;
	private int guestHouseCode;
	private String writer;
	
	
}
