package com.bit.geha.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RoomDtos {
	private List<String> roomName;
	private List<Integer> charge;
	private List<String> gender;
	private List<Integer> capacity;
	private List<String> roomInfo;
	private List<List<MultipartFile>> roomFiles = new ArrayList<>();
	
	private List<MultipartFile> roomAttach;
	private List<Integer> room_imglength;
	private List<Integer> isMainRoomImage;
	
	
	public List<RoomDto> inputRoomDtoValue(int guestHouseCode) {
		List<RoomDto> roomDtos = new ArrayList<>();
		
		setRoomFiles(roomAttach);
		
		for(int i=0; i<roomName.size(); i++) {
			roomDtos.add(new RoomDto(guestHouseCode, roomName.get(i), capacity.get(i), charge.get(i), gender.get(i), roomInfo.get(i)
					, roomFiles.get(i), isMainRoomImage.get(i)));
		}
		
		return roomDtos;
	}
	
	public void setRoomFiles(List<MultipartFile> roomAttach) {
		System.out.println("setRoomFiles -- roomAttach: "+roomAttach);
		
		for(MultipartFile file : roomAttach) {
			System.out.println("RR file: " + file.getOriginalFilename());
		}
		
		int attachIndex = 0;
		System.out.println("room_imglength.size(): " + room_imglength.size());
		for(int i=0; i<room_imglength.size(); i++) {
			System.out.println("room_imglength.get("+i+"): " + room_imglength.get(i));
			List<MultipartFile> tempList = new ArrayList<MultipartFile>();
			for(int j=0; j<room_imglength.get(i); j++) {
				System.out.println("attachIndex: " + attachIndex);
				System.out.println("roomAttach: " + roomAttach.get(attachIndex).getOriginalFilename());
				tempList.add(roomAttach.get(attachIndex++));
			}
			roomFiles.add(tempList);
		}
	}
}
