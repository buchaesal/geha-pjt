package com.bit.geha.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
	
	private int isMainImage;
	private List<MultipartFile> roomFiles;
	
	public RoomDto(int guestHouseCode, String roomName, int capacity, int charge, String gender, String roomInfo, List<MultipartFile> roomFiles, int isMainImage) {
		this.guestHouseCode = guestHouseCode;
		this.roomName = roomName;
		this.capacity = capacity;
		this.charge = charge;
		this.gender = gender;
		this.roomInfo = roomInfo;
		
		this.roomFiles = roomFiles;
		this.isMainImage = isMainImage;
	}
}
