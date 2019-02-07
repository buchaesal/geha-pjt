package com.bit.geha.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RoomDtos {
	private List<String> roomName;
	private List<Integer> charge;
	private List<String> gender;
	private List<Integer> capacity;
	private List<String> roomInfo;
	
	public List<RoomDto> inputRoomDtoValue(int guestHouseCode) {
		List<RoomDto> roomDtos = new ArrayList<>();
		
		for(int i=0; i<roomName.size(); i++) {
			roomDtos.add(new RoomDto(guestHouseCode, roomName.get(i), capacity.get(i), charge.get(i), gender.get(i), roomInfo.get(i)));
		}
		
		return roomDtos;
	}
}
