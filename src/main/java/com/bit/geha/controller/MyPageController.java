package com.bit.geha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit.geha.dao.MyPageDao;

import lombok.extern.java.Log;

@Controller
@RequestMapping(value="/myPage")
@Log
public class MyPageController {
	@Autowired
	MyPageDao myPageDao;
	
	@RequestMapping(value="/bookingList")
	public void bookingList(int memberCode, Model model) {
		log.info("bookingList()");
		
		model.addAttribute("bookingList", myPageDao.getBookingListByMemberCode(memberCode));
		model.addAttribute("reviewList", myPageDao.getReviewListByMemberCode(memberCode));
		model.addAttribute("memberName", myPageDao.getMemberName(memberCode));
	}
	
	@RequestMapping(value="/requestBookingCancel")
	public String requestBookingCancel(int bookingCode, int memberCode) {
		log.info("cancelRequest()");
		log.info("bookingCode: " + bookingCode);
		
		myPageDao.modifyBookingStatus(bookingCode, "취소요청");
		
		return "redirect:/myPage/bookingList?memberCode="+memberCode;//멤버코드는 임시

	}
}
