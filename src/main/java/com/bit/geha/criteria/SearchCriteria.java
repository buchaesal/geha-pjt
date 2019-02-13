package com.bit.geha.criteria;

import java.util.List;

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
	private List<String> gender;
	
	private int trip;
	private int gourmet;
	private int shopping;
	private int business;
	
	private List<Integer> facilities;
	
	private String sort;
}
