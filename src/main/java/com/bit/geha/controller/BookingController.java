package com.bit.geha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.dao.BookingDao;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/booking")
@Log
public class BookingController {
	
	@Autowired
	BookingDao bookingDao;
	
	/*@RequestMapping("/bookingPage")
	public void loadBookingPage(int roomCode, Date checkin, Date checkout, Model model) {
		log.info("loadBookingPage()");
		
		RoomDto roomDto = bookingDao.getRoom(roomCode);
		model.addAttribute("roomDto", roomDto);
	}*/
	/*
	@RequestMapping("/bookingPage")
	public void loadBookingPage(Model model) {
		log.info("loadBookingPage()");
		RoomDto roomDto = bookingDao.getRoom(1);
		String guestHouseName = bookingDao.getGuestHouseName(roomDto.getGuestHouseCode());
		
		model.addAttribute("roomDto", roomDto);
		model.addAttribute("guestHouseName", guestHouseName);
	}
	*/
	@RequestMapping("/booking")
	public void booking() {
		log.info("booking()");
	}
}
