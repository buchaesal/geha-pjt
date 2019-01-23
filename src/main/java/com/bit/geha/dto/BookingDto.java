package com.bit.geha.dto;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class BookingDto {
	private int bookingCode;
	@NonNull
	private int memberCode;
	@NonNull
	private String booker;
	@NonNull
	private String bookerEmail;
	@NonNull
	private String bookerContact;
	@NonNull
	private int bookingNumber;
	@NonNull
	private int roomCode;
	@NonNull
	private Date bookingStart;
	@NonNull
	private Date bookingEnd;
	@NonNull
	private String paymentMethod;
	@NonNull
	private String approvalNumber;
	@NonNull
	private int paymentAmount;
	@NonNull
	private String bookingStatus;
	private Date refundDate;
	private int refundAmount;
}
