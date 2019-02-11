package com.bit.geha.dto;

import lombok.Data;

@Data
public class LikeDto {
	private int roomCode;
	private int memberCode;
	
	//찜한숙소를 위한 추가
	private String guestHouseName;
	private int guestHouseCode;
	private String roomName;
	private int charge;
	private long avgRating;
	
}
