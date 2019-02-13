package com.bit.geha.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ReplyReviewDto {
	private int reviewNo ;
	private String title;
	private double rating;
	private String content;
	private String roomName;
	private String booker;
	private Timestamp writingDate;
	private String replyContent;
	private Timestamp replyDate;
	
	
}
