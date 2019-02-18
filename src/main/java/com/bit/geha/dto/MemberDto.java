package com.bit.geha.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class MemberDto {
	private int memberCode;
	private String id;
	private String password;
	private String memberName;
	private String gender;
	private String authority;
	private String businessLicense;
	private String authCode;
	private String authStatus;
	
	//추가(호스트 정보가져올때)
	private String guestHouseName;
	private String status;
	private int guestHouseCode;
	private Date approvalDate;
	

}
