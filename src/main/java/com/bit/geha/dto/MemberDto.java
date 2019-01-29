package com.bit.geha.dto;

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
}
