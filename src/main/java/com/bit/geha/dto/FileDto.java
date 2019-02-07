package com.bit.geha.dto;

import lombok.Data;

@Data
public class FileDto {
	private String savedName;
	private String originalName;
	private int fileSize;
	private int guestHouseCode;
	private int roomCode;
}
