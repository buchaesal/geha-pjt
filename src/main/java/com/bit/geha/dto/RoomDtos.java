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
	private List<List<MultipartFile>> roomFiles = new ArrayList<>();
	
	private List<MultipartFile> roomAttach;
	private List<Integer> room_imglength;
	private List<Integer> mainRoomImage;
	
	
	public List<RoomDto> inputRoomDtoValue(int guestHouseCode) {
		List<RoomDto> roomDtos = new ArrayList<>();
		
		setRoomFiles(roomAttach);
		
		for(int i=0; i<roomName.size(); i++) {
			
			System.out.println("index: " + i);
			
			System.out.println(
					"roomCode=" + roomCode.get(i)
					+", guestHouseCode=" + guestHouseCode
					+", roomName=" + roomName.get(i)
					+", capacity=" + capacity.get(i)
					+", charge=" + charge.get(i)
					+", gender=" + gender.get(i)
					+", roomInfo=" + roomInfo.get(i)
					+", mainRoomImage=" + mainRoomImage.get(i)
					+", roomFiles=" + (room_imglength.size()==0 || room_imglength.get(i)==null ? null : roomFiles.get(i))
					);
			
			roomDtos.add(new RoomDto(roomCode.get(i), guestHouseCode, roomName.get(i), capacity.get(i), charge.get(i), gender.get(i), roomInfo.get(i), mainRoomImage.get(i)
					, (room_imglength.size()==0 || room_imglength.get(i)==null ? null : roomFiles.get(i))));
		}
		
		return roomDtos;
	}
	
	public void setRoomFiles(List<MultipartFile> roomAttach) {
		
		System.out.println("setRoomFiles -- roomAttach: "+roomAttach);
		
		int attachIndex = 0;
		System.out.println("room_imglength.size(): " + room_imglength.size());
		
		//사진이 아무것도 바뀐게 없으면 반복문 안돌고 그냥 패스
		
		for(int i=0; i<room_imglength.size(); i++) {
			List<MultipartFile> tempList = new ArrayList<MultipartFile>();
			
			System.out.println("roomAttach.size: " + roomAttach.size());
			System.out.println("room_imglength.get("+i+"): " + room_imglength.get(i));
			if(room_imglength.get(i) != null) {
				for(int j=0; j<room_imglength.get(i); j++) {
					System.out.println("attachIndex: " + attachIndex);
					System.out.println("roomAttach: " + roomAttach.get(attachIndex).getOriginalFilename());
					tempList.add(roomAttach.get(attachIndex++));
				}
			} else {
				System.out.println("room_imglength==null roomAttach: " + roomAttach.get(attachIndex++).getOriginalFilename());
			}
			roomFiles.add(tempList);
		}
	}
}
