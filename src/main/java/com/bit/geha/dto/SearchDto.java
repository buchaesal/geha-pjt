package com.bit.geha.dto;

import lombok.Data;

@Data
public class SearchDto {

	private String originalName;
	private int guestHouseCode;
	private String guestHouseName;
	private double avgRating;
	private String recommendRating;
	private int reviewNo;
	private int charge;
	
}
