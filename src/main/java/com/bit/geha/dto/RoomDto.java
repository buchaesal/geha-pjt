package com.bit.geha.dto;

import lombok.Data;

@Data
public class RoomDto {
	private int roomCode;
	private int guestHouseCode;
	private String roomName;
	private int capacity;
	private int charge;
	private String gender;
	private String roomInfo;
}
