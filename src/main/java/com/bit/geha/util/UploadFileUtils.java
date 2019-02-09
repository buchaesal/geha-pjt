package com.bit.geha.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.bit.geha.dto.FileDto;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.RoomDto;

import lombok.extern.java.Log;

@Log
public class UploadFileUtils {
	public static final String UPLOAD_PATH = "C:\\geha_pjt\\";
	
	private static void makeDir(String uploadPath, String ...paths) {
		if(new File(uploadPath + paths[paths.length-1]).exists())
			return;
		
		for(String path:paths) { 
			File dirPath = new File(uploadPath + path);
			
			if(! dirPath.exists()) {
				dirPath.mkdirs();
			}
		}
	}
	

	
	public static List<FileDto> uploadFiles(Object dto) throws Exception {
		List<FileDto> resultList = new ArrayList<FileDto>();
		
		if(dto instanceof GuestHouseDto) {
			GuestHouseDto guestHouseDto = (GuestHouseDto) dto;
			List<MultipartFile> uploadFiles = guestHouseDto.getFiles();
			
			String guestHousePath = guestHouseDto.getGuestHouseCode() + File.separator;
			makeDir(UPLOAD_PATH, guestHousePath);
			
			for(int i=0; i<uploadFiles.size(); i++) {
				MultipartFile file = uploadFiles.get(i);
				UUID uid = UUID.randomUUID();
				
				String savedName = uid.toString() + "_" + file.getOriginalFilename();
				
				File target = new File(UPLOAD_PATH+guestHousePath, savedName);
				file.transferTo(target);
				
				resultList.add(new FileDto(savedName, file.getOriginalFilename(), guestHouseDto.getGuestHouseCode(), ((i==guestHouseDto.getIsMainImage())?true:false)));
			}
			
		} else if(dto instanceof RoomDto) {
			RoomDto roomDto = (RoomDto) dto;
			System.out.println("File Upload RoomDto: " + roomDto.getRoomName());
			List<MultipartFile> uploadFiles = roomDto.getRoomFiles();
			
			String guestHousePath = roomDto.getGuestHouseCode() + File.separator;
			String roomPath = guestHousePath + roomDto.getRoomCode() + File.separator;
			makeDir(UPLOAD_PATH, guestHousePath, roomPath);
			
			for(MultipartFile file : uploadFiles) {
				System.out.println("--uploadFile: " + file.getOriginalFilename());
			}
			
			for(int i=0; i<uploadFiles.size(); i++) {
				MultipartFile file = uploadFiles.get(i);
				
				String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
				
				file.transferTo(new File(UPLOAD_PATH+roomPath, savedName));
				
				resultList.add(new FileDto(savedName, file.getOriginalFilename(), roomDto.getGuestHouseCode(), roomDto.getRoomCode(), ((i==roomDto.getIsMainImage())?true:false)));
			}
		} else return null;
		
		return resultList;
	}
}
