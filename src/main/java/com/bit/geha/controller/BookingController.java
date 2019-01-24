package com.bit.geha.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.dao.BookingDao;
import com.bit.geha.dto.KskRoomDto;

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
	
	@RequestMapping("/bookingPage")
	public void loadBookingPage(@ModelAttribute Date checkin, @ModelAttribute Date checkout, Model model) {
		log.info("loadBookingPage()");
		KskRoomDto roomDto = bookingDao.getRoom(1);
		String guestHouseName = bookingDao.getGuestHouseName(roomDto.getGuestHouseCode());
		
		model.addAttribute("roomDto", roomDto);
		model.addAttribute("guestHouseName", guestHouseName);
	}
	
	@RequestMapping("/booking")
	public void booking() {
		log.info("booking()");
	}
}
