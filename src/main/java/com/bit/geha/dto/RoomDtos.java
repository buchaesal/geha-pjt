package com.bit.geha.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RoomDtos {
	private List<Integer> roomCode;
	private List<String> roomName;
	private List<Integer> charge;
	private List<String> gender;
	private List<Integer> capacity;
	private List<String> roomInfo;
	
	private List<MultipartFile> roomAttach;
	private List<Integer> room_imglength;
	private List<Integer> mainRoomImage;
	
	
	public List<RoomDto> inputRoomDtoValue(int guestHouseCode) {
		List<RoomDto> roomDtos = new ArrayList<>();
		List<List<MultipartFile>> roomFiles = setRoomFiles(roomAttach);
		
		for(int i=0; i<roomName.size(); i++) {
			roomDtos.add(new RoomDto(roomCode.get(i), guestHouseCode, roomName.get(i), capacity.get(i), charge.get(i), gender.get(i), roomInfo.get(i), mainRoomImage.get(i)
					, (room_imglength.size()==0 || room_imglength.get(i)==null ? null : roomFiles.get(i))));
		}
		
		return roomDtos;
	}
	
	public List<List<MultipartFile>> setRoomFiles(List<MultipartFile> roomAttach) {
		List<List<MultipartFile>> roomFiles = new ArrayList<>();
		
		int attachIndex = 0;
		if(roomAttach!=null && room_imglength!=null) { //사진이 바뀐게 있다면
			for(int i=0; i<room_imglength.size(); i++) {
				List<MultipartFile> tempList = new ArrayList<MultipartFile>();
				
				if(room_imglength.get(i) != null) {
					for(int j=0; j<room_imglength.get(i); j++) {
						tempList.add(roomAttach.get(attachIndex++));
					}
				}
				roomFiles.add(tempList);
			}
		}
		
		return roomFiles;
	}
}
