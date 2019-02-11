package com.bit.geha.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.dao.MemberDao;
import com.bit.geha.dao.MyPageDao;
import com.bit.geha.dto.MemberDto;
import com.bit.geha.dto.ReviewDto;
import com.bit.geha.security.SecurityMember;
import com.bit.geha.service.MemberService;

import lombok.extern.java.Log;

@Controller
@RequestMapping(value="/myPage")
@Log
public class MyPageController {
	@Autowired
	MyPageDao myPageDao;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MemberDao memberDao;
	
	//예약내역
	@RequestMapping(value="/bookingList")
	public void bookingList(Model model, HttpSession session, Authentication auth) {
		log.info("bookingList()");
		
		//로그인 계정 가져오기
		memberService.getSession(auth,session);
		int memberCode=((Integer) session.getAttribute("memberCode")).intValue();
		
		model.addAttribute("bookingList", myPageDao.getBookingListByMemberCode(memberCode));
		model.addAttribute("reviewList", myPageDao.getReviewListByMemberCode(memberCode));
		model.addAttribute("memberName", myPageDao.getMemberName(memberCode));
	}
	
	//취소요청
	@RequestMapping(value="/requestBookingCancel")
	@ResponseBody
	public void requestBookingCancel(int bookingCode) {
		log.info("cancelRequest()");
		log.info("bookingCode: " + bookingCode);
		
		myPageDao.modifyBookingStatus(bookingCode, "취소요청");
	}
	
	//리뷰작성
	@RequestMapping(value="/writeReview", method=RequestMethod.POST)
	public String writeReivew(ReviewDto reviewDto, int memberCode) {
		log.info("writeReview()");
		
		System.out.println("reviewDto: " + reviewDto);
		myPageDao.addReview(reviewDto);
		
		//평균평점 구하기
		
		return "redirect:bookingList";
	}
	
	@RequestMapping("/myReview")
	public void myReview(Model model,HttpSession session) {
		int memberCode=((Integer) session.getAttribute("memberCode")).intValue();
		model.addAttribute("reviewList",myPageDao.getReviewList(memberCode));
	}
	
	@RequestMapping("/myInfo")
	public void myInfo(Model model,Authentication auth) {
		SecurityMember sc = (SecurityMember) auth.getPrincipal();
		model.addAttribute("memberDto",memberDao.findById(sc.getUsername()));
	}
	
	@RequestMapping("/updateInfo")
	public String updateInfo(@RequestParam String id,@RequestParam String memberName,@RequestParam String password,
			@RequestParam(required=false) String businessLicense, @RequestParam String gender) {
			if(password=="") {
				
			myPageDao.modifyNameEtc(id, memberName, businessLicense,gender);
			}
			else {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				myPageDao.modifyInfo(id, memberName, 
						passwordEncoder.encode(password), 
						businessLicense,gender);
			}
			
			return "redirect:/myPage/myInfo";
			
	}
	
	@RequestMapping("/modifyReview")
	public String modifyReview(ReviewDto reviewDto) {
		myPageDao.modifyReview(reviewDto);
		return "redirect:/myPage/myReview";
	}
	
	@RequestMapping("/deleteReview")
	public String deleteReview() {
		return "redirect:/myPage/myReview";
	}
}
