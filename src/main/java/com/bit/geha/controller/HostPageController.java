package com.bit.geha.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bit.geha.dao.HostPageDao;
import com.bit.geha.dao.MemberDao;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.RejectDto;
import com.bit.geha.service.MemberService;

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
	
	public static final String UPLOAD_PATH = "C:\\Users\\tmfrl\\git\\geha-pjt\\src\\main\\resources\\static\\gehaImg\\";
	
	@RequestMapping("/myGuestHouseList")
	public void myGuestHouseList(HttpSession session, Authentication auth, Model model) {
		log.info("myGuestHouseList()");
		
		//로그인계정 가져오기
		memberService.getSession(auth,session);
		int memberCode=((Integer) session.getAttribute("memberCode")).intValue();
		
		List<RejectDto> rejectList = hostPageDao.getRejectList(memberCode);
		System.out.println("rejectList.size() : " + rejectList.size());
		
		List<GuestHouseDto> guestHouseList = hostPageDao.getGuestHouseList(memberCode);
		
		model.addAttribute("guestHouseList", guestHouseList);
		model.addAttribute("rejectList", rejectList);
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
	public String registerGuestHouseComplete(@RequestParam List<MultipartFile> roomAttach/*, GuestHouseDto guestHouseDto, RoomDtos roomDtos, @RequestParam List<Integer> facilityCode*/) {
		log.info("registerGuestHouseComplete()");
		
		System.out.println("roomAttach.size(): " + roomAttach.size());
		for(int i=0; i<roomAttach.size(); i++) {
			System.out.println("roomAttach["+i+"]: "+roomAttach);
		}
		
		/*List<MultipartFile> ghAttach = guestHouseDto.getFiles();
		for(int i=0; i<ghAttach.size(); i++) {
			System.out.println(ghAttach.get(i).getOriginalFilename());
		}
		
		hostPageDao.addGuestHouse(guestHouseDto);
		
		Map<String, Object> facilityMap = new HashMap<>();
		facilityMap.put("guestHouseCode", guestHouseDto.getGuestHouseCode());
		facilityMap.put("facilityCode", facilityCode);
		
		hostPageDao.addRooms(roomDtos.inputRoomDtoValue(guestHouseDto.getGuestHouseCode()));
		hostPageDao.addFacilities(facilityMap);*/
		
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
	
	
}
