package com.bit.geha.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class GuestHouseDto {
	private int guestHouseCode;
	private String guestHouseName;
	private String guestHouseInfo;
	private String rules;
	private String parkingInfo;
	private String address;
	private String directions;
	private Date registerDate;
	private Date approvalDate;
	private int memberCode;
	private double avgRating;
	private byte businessTrip;
	private byte gourmet;
	private byte trip;
	private byte shopping;
	private String bankName;
	private String accountNumber;
	
}
