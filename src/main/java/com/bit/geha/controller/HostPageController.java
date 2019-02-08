package com.bit.geha.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.dao.HostPageDao;
import com.bit.geha.dao.MemberDao;
import com.bit.geha.dto.FileDto;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.RejectDto;
import com.bit.geha.dto.RoomDto;
import com.bit.geha.dto.RoomDtos;
import com.bit.geha.service.MemberService;
import com.bit.geha.util.UploadFileUtils;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/hostPage")
@Log
public class HostPageController {
	@Autowired
	HostPageDao hostPageDao;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MemberDao memberDao;
	
	
	@RequestMapping("/myGuestHouseList")
	public void myGuestHouseList(HttpSession session, Authentication auth, Model model) {
		log.info("myGuestHouseList()");
		
		//로그인계정 가져오기
		memberService.getSession(auth,session);
		int memberCode=((Integer) session.getAttribute("memberCode")).intValue();
		
		List<Integer> isRejectList = hostPageDao.getIsRejectList(memberCode);
		System.out.println("rejectList.size() : " + isRejectList.size());
		System.out.println(isRejectList);
		
		List<GuestHouseDto> guestHouseList = hostPageDao.getGuestHouseList(memberCode);
		
		model.addAttribute("guestHouseList", guestHouseList);
		model.addAttribute("isRejectList", isRejectList);
	}
	
	@RequestMapping("getRejectList")
	@ResponseBody
	public List<RejectDto> getRejectList(int guestHouseCode) {
		log.info("getRejectList()");
		
		return hostPageDao.getRejectListByGuestHouseCode(guestHouseCode);
	}
	
	@RequestMapping("/registerGuestHouse")
	public void registerGuestHouse(HttpSession session, Authentication auth, Model model) {
		log.info("registerGuestHouse()");
		
		//로그인계정 가져오기
		memberService.getSession(auth,session);
		int memberCode=((Integer) session.getAttribute("memberCode")).intValue();
		model.addAttribute("memberCode", memberCode);
	}
	
	@RequestMapping(value="/registerGuestHouseComplete", method=RequestMethod.POST)
	public String registerGuestHouseComplete(GuestHouseDto guestHouseDto, RoomDtos roomDtos, @RequestParam List<Integer> facilityCode) throws Exception {
		log.info("registerGuestHouseComplete()");
		
		//게스트하우스 insert
		hostPageDao.addGuestHouse(guestHouseDto);
		
		//편의시설 insert
		Map<String, Object> facilityMap = new HashMap<>();
		facilityMap.put("guestHouseCode", guestHouseDto.getGuestHouseCode());
		facilityMap.put("facilityCode", facilityCode);
		hostPageDao.addFacilities(facilityMap);
		
		//방 insert
		List<RoomDto> roomList = roomDtos.inputRoomDtoValue(guestHouseDto.getGuestHouseCode());
		hostPageDao.addRooms(roomList);
		
		//파일 insert
		List<FileDto> guestHouseFiles = UploadFileUtils.uploadFiles(guestHouseDto);
		hostPageDao.addFiles(guestHouseFiles);
		
		for(RoomDto roomDto : roomList) {
			List<FileDto> roomFiles = UploadFileUtils.uploadFiles(roomDto);
			hostPageDao.addFiles(roomFiles);
		}
		
		return "redirect:/hostPage/myGuestHouseList";
	}
	
	@RequestMapping(value = "/jusoPopup", method = RequestMethod.GET)
	public void jusoPopup() {
		System.out.println("method.GET");
	}
	
	@RequestMapping(value = "/jusoPopup", method = RequestMethod.POST)
	public void jusoPopup(Model model) {
		System.out.println("method.POST");
	}
	
	@RequestMapping(value="/testPost")
	public void testPost() {
		System.out.println("GET()");
	}
	
	@RequestMapping(value="testPost2", method=RequestMethod.POST)
	public String testPost2(String hello) {
		System.out.println("POST()");
		return "redirect:testPost";
	}
}
