package com.bit.geha.dto;

import java.sql.Date;
import lombok.Data;

@Data
public class RejectDto {
	private int guestHouseCode;
	private int memberCode;
	private Date rejectDate;
	private String rejectMsg;
}
