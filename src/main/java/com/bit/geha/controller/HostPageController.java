package com.bit.geha.controller;

import java.util.ArrayList;
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
		List<RoomDto> roomList = hostPageDao.getRooms(guestHouseCode);
		model.addAttribute("facilities", hostPageDao.getFacilities(guestHouseCode));
		model.addAttribute("guestHouseImgs", hostPageDao.getImgs(guestHouseCode, 0));
		
		for(int i=0; i<roomList.size(); i++) {
			roomList.get(i).setRoomImgs(hostPageDao.getImgs(guestHouseCode, roomList.get(i).getRoomCode()));
		}
		model.addAttribute("rooms", roomList);
	}
	
	@RequestMapping("modifyGuestHouseComplete")
	public String modifyGuestHouseComplete(GuestHouseDto guestHouseDto, RoomDtos roomDtos, @RequestParam List<Integer> facilityCode) throws Exception {
		log.info("modifyGuestHouseComplete()");

		System.out.println("GuestHouseCode: " + guestHouseDto.getGuestHouseCode());
		
		/*
		 * 게스트하우스 무조건 update
		 * File savedName이 하나라도 있으면 안바뀜 (mainImage도 변경 여부) / 없으면 바뀜 기존-delete 수정-insert
		 * facility 무조건 update
		 * room 하나하나 비교
		 * 수정(roomCode != 0) - File : savedName이 하나라도 있으면 안바뀜 / 없으면 바뀜 기존-delete 수정-insert mainImage도 변경
		 * 					 - RoomDto : 무조건 update
		 * 삭제(!roomCode.exists())  - File : 무조건 delete
		 * 							- RoomDto : 무조건  delete
		 * 추가(roomCode == 0) - File : 무조건 insert
		 * 					  - RoomDto : 무조건 insert 
		*/
		
		//게스트하우스 update
		hostPageDao.modifyGuestHouse(guestHouseDto);
		
		List<FileDto> existingGuestHouseImgs = hostPageDao.getImgs(guestHouseDto.getGuestHouseCode(), 0);
		
		if(guestHouseDto.getFiles().get(0).getSize() == 0) { //기존이미지였을 경우
			System.out.println("게스트하우스 기존이미지");
			
			if(!(existingGuestHouseImgs.get(guestHouseDto.getMainImage()).isMainImage())) { //기존 메인인덱스랑 다를 경우
				for(int i=0; i<existingGuestHouseImgs.size(); i++) {
					System.out.println("메인인덱스랑 다름");
					if(i==guestHouseDto.getMainImage()) {
						existingGuestHouseImgs.get(i).setMainImage(true);
						hostPageDao.modifyMainImage(existingGuestHouseImgs.get(i));
					} else {
						existingGuestHouseImgs.get(i).setMainImage(false);
						hostPageDao.modifyMainImage(existingGuestHouseImgs.get(i));
					}
				}
			}
		} else { //새로운 이미지일 경우
			System.out.println("게스트하우스 새로운이미지");
			
			//파일 delete
			UploadFileUtils.deleteFiles(existingGuestHouseImgs);
			hostPageDao.deleteImgs(guestHouseDto.getGuestHouseCode(), 0);
			
			//파일 insert
			List<FileDto> guestHouseFiles = UploadFileUtils.uploadFiles(guestHouseDto);
			hostPageDao.addFiles(guestHouseFiles);
		}

		
		//편의시설 delete and insert
		hostPageDao.deleteFacilities(guestHouseDto.getGuestHouseCode());
		Map<String, Object> facilityMap = new HashMap<>();
		facilityMap.put("guestHouseCode", guestHouseDto.getGuestHouseCode());
		facilityMap.put("facilityCode", facilityCode);
		hostPageDao.addFacilities(facilityMap);
		
		
		//수정화면에서 업로드 된 방
		List<RoomDto> rooms = roomDtos.inputRoomDtoValue(guestHouseDto.getGuestHouseCode());
		
		List<RoomDto> newRooms = new ArrayList<>(); //추가 방 List
		List<Integer> existingRoomCodes = hostPageDao.getRoomCodes(guestHouseDto.getGuestHouseCode());
		
		for(RoomDto room : rooms) {
			if(existingRoomCodes.contains(room.getRoomCode())) { //수정되었을 경우
				System.out.println("기존 룸 : " + room.getRoomCode());
				
				existingRoomCodes.remove((Integer)room.getRoomCode()); //존재하는 것들은 지움
				
				List<FileDto> existingRoomImgs = hostPageDao.getImgs(room.getGuestHouseCode(), room.getRoomCode());

				if(room.getRoomFiles()==null || room.getRoomFiles().get(0).getSize() == 0) { //기존이미지였을 경우
					System.out.println("방 기존이미지");
					
					if(room.getMainImage()!=-1) { //기존 메인인덱스랑 다를 경우
						for(int i=0; i<existingRoomImgs.size(); i++) {
							System.out.println("메인인덱스랑 다름");
							if(i==room.getMainImage()) {
								existingRoomImgs.get(i).setMainImage(true);
								hostPageDao.modifyMainImage(existingRoomImgs.get(i));
							} else {
								existingRoomImgs.get(i).setMainImage(false);
								hostPageDao.modifyMainImage(existingRoomImgs.get(i));
							}
						}
					}
				} else { //새로운 이미지일 경우
					System.out.println("방 새로운이미지");
					
					//파일 delete
					UploadFileUtils.deleteFiles(existingRoomImgs);
					hostPageDao.deleteImgs(room.getGuestHouseCode(), room.getRoomCode());
					
					//파일 insert
					List<FileDto> roomFiles = UploadFileUtils.uploadFiles(room);
					hostPageDao.addFiles(roomFiles);
				}
				
				//방 update
				hostPageDao.modifyRoom(room);
			} else {
				//추가 되었을 경우
				newRooms.add(room);
			}
		}
		
		//방 insert
		if(newRooms.size()>0) {
			hostPageDao.addRooms(newRooms);
			
			for(RoomDto roomDto : newRooms) {
				List<FileDto> roomFiles = UploadFileUtils.uploadFiles(roomDto);
				hostPageDao.addFiles(roomFiles);
			}
		}
		
		//삭제되었을 경우 - File, Room delete
		System.out.println("delete roomCode: " + existingRoomCodes);
		if(existingRoomCodes.size()>0) {
			hostPageDao.deleteRooms(existingRoomCodes);
			for (int i = 0; i < existingRoomCodes.size(); i++) {
				System.out.println("delete Img");
				hostPageDao.deleteImgs(guestHouseDto.getGuestHouseCode(), existingRoomCodes.get(i));
				UploadFileUtils.deleteRoomFiles(guestHouseDto.getGuestHouseCode(), existingRoomCodes.get(i));
			}
		}
		
		return "redirect:myGuestHouseList";
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
