package com.bit.geha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit.geha.dao.RoomDao;
import com.bit.geha.dto.FacilityDto;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.RoomDto;

@Controller
public class RoomController {
	
	@Autowired
	RoomDao roomDao;
	
	
	@RequestMapping("/room")
	public String roomInfo() {
		
		return "/room/roomInfo";
	}
	
    @RequestMapping("/gehaList")
    public String guestHouseList(Model model) {
    	List<GuestHouseDto> geha = roomDao.selectGuestHouse();
    	model.addAttribute("geha", geha);
    	
    	return "/room/gehaList_test";
    	
    }
    
    @RequestMapping("/roomInfo")
    public String guestHouseInfo(@RequestParam("guestHouseCode") int guestHouseCode, Model model) {
    	List<RoomDto> rooms = roomDao.roomInfo(guestHouseCode);
    	List<FacilityDto> facility = roomDao.facilityInfo(guestHouseCode);
    	
    	model.addAttribute("geha", roomDao.gehaInfo(guestHouseCode));
    	model.addAttribute("room", rooms);
    	model.addAttribute("facility", facility );
    	
    	return "/room/roomInfo";
    	
    }
}
