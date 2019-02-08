package com.bit.geha.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit.geha.dao.CommentDao;
import com.bit.geha.dao.RoomDao;
import com.bit.geha.dto.FacilityDto;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.RoomDto;
import com.bit.geha.service.MemberService;



@Controller
@RequestMapping(value="/room")
public class RoomController {

	@Autowired
	RoomDao roomDao;
	@Autowired
	CommentDao commentDao;
	@Autowired
	MemberService memberService;
	
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
    public String guestHouseInfo(@RequestParam("guestHouseCode") int guestHouseCode, Authentication auth,HttpSession session,Model model) throws Exception {
    	memberService.getSession(auth, session);
    	List<RoomDto> rooms = roomDao.roomInfo(guestHouseCode);
    	List<FacilityDto> facility = roomDao.facilityInfo(guestHouseCode);

    	model.addAttribute("rating" , commentDao.commentCnt(guestHouseCode));
    	model.addAttribute("geha", roomDao.gehaInfo(guestHouseCode));
    	model.addAttribute("room", rooms);
    	model.addAttribute("facility", facility );

    	return "/room/roomInfo";
    	
    }
    

    
}
