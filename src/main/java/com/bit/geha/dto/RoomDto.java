package com.bit.geha.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
	
	private int mainImage;
	//업로드
	private List<MultipartFile> roomFiles;
	//다운로드
	private List<FileDto> roomImgs;
	
	public RoomDto(int roomCode, int guestHouseCode, String roomName, int capacity, int charge, String gender, String roomInfo, int mainImage, List<MultipartFile> roomFiles) {
		this.roomCode = roomCode;
		this.guestHouseCode = guestHouseCode;
		this.roomName = roomName;
		this.capacity = capacity;
		this.charge = charge;
		this.gender = gender;
		this.roomInfo = roomInfo;
		this.mainImage = mainImage;
		this.roomFiles = roomFiles;
	}
	
}
