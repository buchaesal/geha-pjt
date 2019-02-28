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

import com.bit.geha.criteria.AdminPageCriteria;
import com.bit.geha.dao.HostPageDao;
import com.bit.geha.dao.MemberDao;
import com.bit.geha.dao.MyPageDao;
import com.bit.geha.dto.FileDto;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.RejectDto;
import com.bit.geha.dto.RoomDto;
import com.bit.geha.dto.RoomDtos;
import com.bit.geha.service.MemberService;
import com.bit.geha.util.PageMaker;
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
	public void myGuestHouseList(HttpSession session, Authentication auth, Model model,
			@ModelAttribute("cri") AdminPageCriteria cri) {
		log.info("myGuestHouseList()");
		cri.setPerPageNum(5);
		
		//로그인계정 가져오기
		memberService.getSession(auth,session);
		int memberCode=((Integer) session.getAttribute("memberCode")).intValue();
		
		List<GuestHouseDto> guestHouseList = hostPageDao.getGuestHouseList(cri, memberCode);
		
		model.addAttribute("rejectedGuestHouseList", hostPageDao.getRejectedGuestHouseCodeList());
		
		// 페이징
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(hostPageDao.getGuestHouseListTotal(memberCode));

		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("guestHouseList", guestHouseList);
		model.addAttribute("tomorrow", memberService.getTomorrow());
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
	public String modifyGuestHouseComplete(GuestHouseDto guestHouseDto, RoomDtos roomDtos, @RequestParam List<Integer> facilityCode, boolean isReapply) throws Exception {
		log.info("modifyGuestHouseComplete()");
		
		//게스트하우스 update
		hostPageDao.modifyGuestHouse(guestHouseDto, isReapply);
		
		List<FileDto> existingGuestHouseImgs = hostPageDao.getImgs(guestHouseDto.getGuestHouseCode(), 0);
		
		if(guestHouseDto.getFiles().get(0).getSize() == 0) { //기존이미지였을 경우
			if(!(existingGuestHouseImgs.get(guestHouseDto.getMainImage()).isMainImage())) { //기존 메인인덱스랑 다를 경우
				for(int i=0; i<existingGuestHouseImgs.size(); i++) {
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
				existingRoomCodes.remove((Integer)room.getRoomCode()); //존재하는 것들은 지움
				
				List<FileDto> existingRoomImgs = hostPageDao.getImgs(room.getGuestHouseCode(), room.getRoomCode());

				if(room.getRoomFiles()==null || room.getRoomFiles().get(0).getSize() == 0) { //기존이미지였을 경우
					if(room.getMainImage()!=-1) { //기존 메인인덱스랑 다를 경우
						for(int i=0; i<existingRoomImgs.size(); i++) {
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
		if(existingRoomCodes.size()>0) {
			hostPageDao.deleteRooms(existingRoomCodes);
			for (int i = 0; i < existingRoomCodes.size(); i++) {
				hostPageDao.deleteImgs(guestHouseDto.getGuestHouseCode(), existingRoomCodes.get(i));
				UploadFileUtils.deleteRoomImgFolder(guestHouseDto.getGuestHouseCode(), existingRoomCodes.get(i));
			}
		}
		
		return "redirect:myGuestHouseList";
	}
	
	@RequestMapping("deleteGuestHouse")
	public String deleteGuestHouse(int guestHouseCode) {
		log.info("deleteGuestHouse()");
		log.info("guestHouseCode: " + guestHouseCode);
		
		List<Integer> deleteRoomCodes = hostPageDao.getRoomCodes(guestHouseCode);
		
		if(deleteRoomCodes.size()>0) {
			hostPageDao.deleteLike(deleteRoomCodes); //좋아요 삭제
			hostPageDao.deleteRooms(deleteRoomCodes); //방 삭제
			for(int roomCode : deleteRoomCodes) {
				hostPageDao.deleteImgs(guestHouseCode, roomCode); //db에서 파일내역 지우기
				UploadFileUtils.deleteRoomImgFolder(guestHouseCode, roomCode); //경로에서 이미지 지우기
			}
		}
		
		//편의시설 delete
		hostPageDao.deleteFacilities(guestHouseCode);
		
		//게스트하우스 파일 delete
		hostPageDao.deleteImgs(guestHouseCode, 0);
		UploadFileUtils.deleteGuestHouseImgFolder(guestHouseCode);
		
		hostPageDao.deleteGuestHouse(guestHouseCode);
		
		return "redirect:myGuestHouseList";
	}
	
	@RequestMapping(value="guestBookingList")
	public void guestBookingList(HttpSession session, Authentication auth, Model model, @ModelAttribute("cri") AdminPageCriteria cri) {
		log.info("guestBookingList()");
		cri.setPerPageNum(5);

		if(cri.getType()==null) {
			cri.setType("");
			cri.setKeyword("");
		}
		
		
		//로그인계정 가져오기
		memberService.getSession(auth,session);
		int memberCode=((Integer) session.getAttribute("memberCode")).intValue();
		
		model.addAttribute("guestBookingList", hostPageDao.getGuestBookingList(cri, memberCode));
		
		// 페이징
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(hostPageDao.getGuestBookingListTotal(cri, memberCode));

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
	public void guestReviewList(HttpSession session, Authentication auth, Model model, @ModelAttribute("cri") AdminPageCriteria cri) {
		log.info("guestReviewList()");
		cri.setPerPageNum(10);
		
		//로그인계정 가져오기
		memberService.getSession(auth,session);
		int memberCode=((Integer) session.getAttribute("memberCode")).intValue();
		
		model.addAttribute("guestReviewList", hostPageDao.getGuestReviewList(cri, memberCode));
		
		// 페이징
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(hostPageDao.getGuestReviewListTotal(memberCode));

		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("tomorrow", memberService.getTomorrow());
	}
}
