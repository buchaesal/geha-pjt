package com.bit.geha.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchCriteria {
	private String bookingStart;
	private String bookingEnd;
	private String keyword;
	private int bookingNumber;
	private int minprice;
	private int maxprice;
	private String gender;
	
	private int trip;
	private int gourmet;
	private int shopping;
	private int business;
	
	private int facilities;
}
