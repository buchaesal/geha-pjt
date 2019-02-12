package com.bit.geha.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.dao.HostPageDao;
import com.bit.geha.dao.MemberDao;
import com.bit.geha.dao.MyPageDao;
import com.bit.geha.dto.BoardCriteria;
import com.bit.geha.dto.FileDto;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.PageMaker;
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
	MyPageDao myPageDao;
	
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
	
	@RequestMapping("modifyGuestHouse")
	public void modifyGuestHouse(@ModelAttribute("guestHouseCode") int guestHouseCode, Model model) {
		log.info("modifyGuestHouse()");
		
		model.addAttribute("guestHouseDto", hostPageDao.getGuestHouse(guestHouseCode));
		model.addAttribute("rooms", hostPageDao.getRooms(guestHouseCode));
		model.addAttribute("facilities", hostPageDao.getFacilities(guestHouseCode));
		List<FileDto> guestHouseImgs = hostPageDao.getGuestHouseImgs(guestHouseCode);
		for(FileDto img : guestHouseImgs) {
			System.out.println(img.getSavedName());
		}
		model.addAttribute("guestHouseImgs", guestHouseImgs);
		
	}
	
	@RequestMapping(value="guestBookingList")
	public void guestBookingList(@ModelAttribute("cri") BoardCriteria cri, HttpSession session, Authentication auth, Model model) {
		log.info("guestBookingList()");
		
		//로그인계정 가져오기
		memberService.getSession(auth,session);
		int memberCode=((Integer) session.getAttribute("memberCode")).intValue();
		
		model.addAttribute("guestBookingList", hostPageDao.getGuestBookingList(memberCode, cri));
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(hostPageDao.getGuestBookingListCount(memberCode)); //파라미터 추가로 boardSearchCriteria 추가해야됨
		
		model.addAttribute("pageMaker", pageMaker);
	}
	
	@RequestMapping("doBookingCancel")
	@ResponseBody
	public void doBookingCancel(int bookingCode) {
		log.info("doBookingCancel()");
		myPageDao.modifyBookingStatus(bookingCode, "취소완료");
	}
	
	@RequestMapping("doEarlyCheckout")
	@ResponseBody
	public void doEarlyCheckout(int bookingCode) {
		log.info("doEarlyCheckout()");
		hostPageDao.modifyBookingStatusToCheckout(bookingCode);
	}
	
	@RequestMapping(value="guestReviewList")
	public void guestReviewList(HttpSession session, Authentication auth, Model model) {
		log.info("guestReviewList()");
		
		//로그인계정 가져오기
		memberService.getSession(auth,session);
		int memberCode=((Integer) session.getAttribute("memberCode")).intValue();
		
		model.addAttribute("guestReviewList", hostPageDao.getGuestReviewList(memberCode));
	}
}
