package com.bit.geha.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit.geha.dao.BookingDao;
import com.bit.geha.dto.BookingDto;
import com.bit.geha.dto.RoomDto;

import lombok.extern.java.Log;

@Controller
@RequestMapping(value="/booking")
@Log
public class BookingController {
	
	@Autowired
	BookingDao bookingDao;
	
	@RequestMapping("/bookingPage")
	public void loadBookingPage(Model model, int roomCode
			, @RequestParam(value="bookingStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date bookingStart
			, @RequestParam(value="bookingEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date bookingEnd) {
		log.info("loadBookingPage()");
		RoomDto roomDto = bookingDao.getRoom(roomCode);
		log.info("roomDto: " + roomDto);
		String guestHouseName = bookingDao.getGuestHouseNameByGuestHouseCode(roomDto.getGuestHouseCode());
		log.info("guestHouseName: " + guestHouseName);
		
		
		model.addAttribute("checkin", bookingStart);
		model.addAttribute("checkout", bookingEnd);
		model.addAttribute("roomDto", roomDto);
		model.addAttribute("guestHouseName", guestHouseName);
	}
	
	@RequestMapping(value="/bookingComplete", method=RequestMethod.POST)
	public String bookingComplete(BookingDto bookingDto) {
		log.info("bookingComplete()");
		System.out.println(bookingDto);
		
		bookingDao.addBooking(bookingDto);
		int bookingCode = bookingDto.getBookingCode();
		
		System.out.println("bookingCode: " + bookingCode);
		return "redirect:/booking/bookingDetail?bookingCode="+bookingCode;
	}
	
	@RequestMapping("/bookingDetail")
	public void bookingDetail(int bookingCode, Model model) {
		log.info("bookingDetail()");
		
		BookingDto bookingDto = bookingDao.getBooking(bookingCode);
		bookingDto.setGuestHouseName(bookingDao.getGuestHouseNameByBookingCode(bookingCode));
		bookingDto.setRoomName(bookingDao.getRoomNameByBookingCode(bookingCode));
		
		model.addAttribute("bookingDto", bookingDto);
	}
}
