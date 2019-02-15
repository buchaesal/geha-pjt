package com.bit.geha.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.bit.geha.dto.FileDto;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.RoomDto;

import lombok.extern.java.Log;

@Log
public class UploadFileUtils {
//	public static final String UPLOAD_PATH = "C:\\Users\\tmfrl\\git\\geha-pjt\\src\\main\\resources\\static\\gehaImg\\";
	
	public static String makeUploadRootPath() {
		String uploadRootPath = null;

		final DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
		
		Resource resource = (Resource) defaultResourceLoader.getResource("file:src\\main\\resources\\static\\gehaImg\\");
		try {
			uploadRootPath = resource.getFile().getAbsolutePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("uploadRootPath: " + uploadRootPath);
		
		return uploadRootPath;
	}
	
	
	
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
		String uploadPath = makeUploadRootPath() ;
		
		if(dto instanceof GuestHouseDto) {
			GuestHouseDto guestHouseDto = (GuestHouseDto) dto;
			List<MultipartFile> uploadFiles = guestHouseDto.getFiles();
			
			String guestHousePath = File.separator + guestHouseDto.getGuestHouseCode() + File.separator;
			System.out.println();
			makeDir(uploadPath , guestHousePath);
			
			for(int i=0; i<uploadFiles.size(); i++) {
				MultipartFile file = uploadFiles.get(i);
				UUID uid = UUID.randomUUID();
				
				String savedName = uid.toString() + "_" + file.getOriginalFilename();
				
				File target = new File(uploadPath+guestHousePath, savedName);
				file.transferTo(target);
				
				resultList.add(new FileDto(savedName, file.getOriginalFilename(), guestHouseDto.getGuestHouseCode(), ((i==guestHouseDto.getMainImage())?true:false)));
			}
			
		} else if(dto instanceof RoomDto) {
			RoomDto roomDto = (RoomDto) dto;
			System.out.println("File Upload RoomDto: " + roomDto.getRoomName());
			List<MultipartFile> uploadFiles = roomDto.getRoomFiles();
			
			String guestHousePath = File.separator + roomDto.getGuestHouseCode() + File.separator;
			String roomPath = guestHousePath + roomDto.getRoomCode() + File.separator;
			makeDir(uploadPath, guestHousePath, roomPath);
			
			for(MultipartFile file : uploadFiles) {
				System.out.println("--uploadFile: " + file.getOriginalFilename());
			}
			
			for(int i=0; i<uploadFiles.size(); i++) {
				MultipartFile file = uploadFiles.get(i);
				
				String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
				
				file.transferTo(new File(uploadPath+roomPath, savedName));
				
				resultList.add(new FileDto(savedName, file.getOriginalFilename(), roomDto.getGuestHouseCode(), roomDto.getRoomCode(), ((i==roomDto.getMainImage())?true:false)));
			}
		} else return null;
		
		for(int i=0; i<resultList.size(); i++) {
			System.out.println("upload File : " + resultList.get(i).getOriginalName());
		}
		return resultList;
	}
	
	public static void deleteFiles(List<FileDto> files) {
		if(files==null || files.size()==0) return;
		
		String uploadPath = makeUploadRootPath() ;
		
		if(files.get(0).getRoomCode() == 0) { //게스트하우스 이미지
			System.out.println("게하 이미지");
			for(FileDto file : files) {
				String path = uploadPath + file.getGuestHouseCode() + File.separator + file.getSavedName();
				System.out.println("deletePath: " + path);
				
				File deleteFile = new File(path);
				if(deleteFile.exists()) {
					deleteFile.delete();
				}
			}
		} else { //방 이미지
			System.out.println("방 이미지");
			for(FileDto file : files) {
				String path = uploadPath + file.getGuestHouseCode() + File.separator + file.getRoomCode() + File.separator + file.getSavedName();
				System.out.println("deletePath: " + path);
				
				File deleteFile = new File(path);
				if(deleteFile.exists()) {
					deleteFile.delete();
				}
			}
		}
	}
	
	public static void deleteRoomImgFolder(int guestHouseCode, int roomCode) {
		String uploadPath = makeUploadRootPath() ;
		
		String path = uploadPath + guestHouseCode + File.separator + roomCode; //폴더 삭제
		System.out.println("deletePath: " + path);
		
		deleteFolder(path);
	}
	
	public static void deleteGuestHouseImgFolder(int guestHouseCode) {
		String uploadPath = makeUploadRootPath() ;
		
		String path = uploadPath + guestHouseCode; //폴더 삭제
		System.out.println("deletePath: " + path);
		
		deleteFolder(path);
	}
	
/*	public static void deleteFolder(int guestHouseCode, int roomCode) {
		String path = uploadPath + guestHouseCode + File.separator + roomCode; //폴더 삭제
		System.out.println("deletePath: " + path);
		
		File deleteFile = new File(path);
		if(deleteFile.exists()) {
			try {
				FileUtils.forceDelete(deleteFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/
	
	private static void deleteFolder(String path) {
		File deleteFile = new File(path);
		if(deleteFile.exists()) {
			try {
				FileUtils.forceDelete(deleteFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
