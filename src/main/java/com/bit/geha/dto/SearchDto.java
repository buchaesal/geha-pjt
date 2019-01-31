package com.bit.geha.dto;

import lombok.Data;

@Data
public class SearchDto {

	private int guestHouseCode;
	private String guestHouseName;
	private double avgRating;
	private String recommendRating;
	private int minprice;
	private int businesstrip;
	private int gourmet;
	private int trip;
	private int shopping;
	private int reviewCnt;
	private String originalName;
	
}
