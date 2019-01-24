package com.bit.geha.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("room")
@Data
public class RoomDto {

	private int roomCode ;
	private int guestHouseCode;
	private String roomName;
	private int capacity;
	private int charge;
	private String gender;
	private String roomInfo;
	
	
}
