package com.bit.geha.dto;

import lombok.Data;

@Data
public class SearchDto {
	private int rownum;
	private int guestHouseCode;
	private String guestHouseName;
	private double avgRating;
	private String recommendRating;
	private int minprice;
	private int businesstrip;
	private Integer gourmet;
	private Integer trip;
	private Integer shopping;
	private Integer reviewCnt;
	private String address;
	private String savedName;
	
}
