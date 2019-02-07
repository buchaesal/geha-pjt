package com.bit.geha.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomDto {

	private int roomCode ;
	private int guestHouseCode;
	private String roomName;
	private int capacity;
	private int charge;
	private String gender;
	private String roomInfo;
	
	public RoomDto(int guestHouseCode, String roomName, int capacity, int charge, String gender, String roomInfo) {
		this.guestHouseCode = guestHouseCode;
		this.roomName = roomName;
		this.capacity = capacity;
		this.charge = charge;
		this.gender = gender;
		this.roomInfo = roomInfo;
	}
}
