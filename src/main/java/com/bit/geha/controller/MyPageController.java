package com.bit.geha.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bit.geha.criteria.AdminPageCriteria;
import com.bit.geha.dao.MemberDao;
import com.bit.geha.dao.MyPageDao;
import com.bit.geha.dto.FileDto;
import com.bit.geha.dto.LikeDto;
import com.bit.geha.dto.ReviewDto;
import com.bit.geha.security.SecurityMember;
import com.bit.geha.service.MemberService;
import com.bit.geha.util.PageMaker;

import lombok.extern.java.Log;

@Controller
@RequestMapping(value = "/myPage")
@Log
public class MyPageController {
	@Autowired
	MyPageDao myPageDao;

	@Autowired
	MemberService memberService;

	@Autowired
	MemberDao memberDao;

	// 예약내역
	@GetMapping(value = "/bookingList")
	public void bookingList(Model model, HttpSession session, Authentication auth,
			@ModelAttribute("cri") AdminPageCriteria cri) {
		log.info("bookingList()");
		cri.setPerPageNum(5);

		// 로그인 계정 가져오기
		memberService.getSession(auth, session);
		int memberCode = ((Integer) session.getAttribute("memberCode")).intValue();

		// 페이징
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(myPageDao.getBookingListTotal(memberCode));

		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("bookingList", myPageDao.getBookingListByMemberCode(cri, memberCode));
		model.addAttribute("reviewList", myPageDao.getReviewListByMemberCode(memberCode));
	}

	// 취소요청
	@RequestMapping(value = "/requestBookingCancel")
	@ResponseBody
	public void requestBookingCancel(int bookingCode) {
		log.info("cancelRequest()");
		log.info("bookingCode: " + bookingCode);

		myPageDao.modifyBookingStatus(bookingCode, "취소요청");
	}

	// 리뷰작성
	@RequestMapping(value = "/writeReview", method = RequestMethod.POST)
	public String writeReivew(ReviewDto reviewDto, int memberCode) {
		log.info("writeReview()");

		System.out.println("reviewDto: " + reviewDto);
		myPageDao.addReview(reviewDto);

		// 평균평점 구하기

		return "redirect:bookingList";
	}

	// 리뷰관리
	@RequestMapping("/myReview")
	public void myReview(Model model, HttpSession session, @ModelAttribute("cri") AdminPageCriteria cri) {
		int memberCode = ((Integer) session.getAttribute("memberCode")).intValue();
		cri.setPerPageNum(5);
		model.addAttribute("reviewList", myPageDao.getReviewList(cri, memberCode));

		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(myPageDao.getReviewTotal(memberCode));
		model.addAttribute("pageMaker", pageMaker);

		model.addAttribute("tomorrow", memberService.getTomorrow());
	}

	// 내정보관리
	@RequestMapping("/myInfo")
	public void myInfo(Model model, Authentication auth) {
		SecurityMember sc = (SecurityMember) auth.getPrincipal();
		model.addAttribute("memberDto", memberDao.findById(sc.getUsername()));

	}

	// 내정보수정
	@RequestMapping("/updateInfo")
	public String updateInfo(@RequestParam String id, @RequestParam String memberName, @RequestParam String password,
			@RequestParam(required = false) String businessLicense, @RequestParam String gender,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if (password == "") {

			myPageDao.modifyNameEtc(id, memberName, businessLicense, gender);
		} else {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			myPageDao.modifyInfo(id, memberName, passwordEncoder.encode(password), businessLicense, gender);
		}

		session.removeAttribute("name");
		session.setAttribute("name", memberName);

		redirectAttributes.addFlashAttribute("updateInfo", "정보가 수정되었습니다!");
		return "redirect:/myPage/myInfo";

	}

	// 리뷰 수정
	@RequestMapping("/modifyReview")
	public String modifyReview(ReviewDto reviewDto, RedirectAttributes redirectAttributes) {
		myPageDao.modifyReview(reviewDto);
		redirectAttributes.addFlashAttribute("modifyOk", "리뷰가 수정되었습니다!");
		return "redirect:/myPage/myReview";
	}

	// 리뷰 삭제
	@RequestMapping("/deleteReview")
	public String deleteReview(@RequestParam int reviewNo, RedirectAttributes redirectAttributes) {
		myPageDao.deleteReview(reviewNo);
		redirectAttributes.addFlashAttribute("deleteInfo", "선택한 후기가 삭제되었습니다.");
		return "redirect:/myPage/myReview";
	}

	// 찜한숙소
	@GetMapping("/myLike")
	public void myLike(Model model, HttpSession session, @ModelAttribute("cri") AdminPageCriteria cri) {
		int memberCode = ((Integer) session.getAttribute("memberCode")).intValue();
		cri.setPerPageNum(5);

		HashMap<Integer, List<FileDto>> map = new HashMap<Integer, List<FileDto>>();
		List<LikeDto> likeDtoList = myPageDao.myLike(cri, memberCode);
		for (LikeDto list : likeDtoList) {

			map.put(list.getGuestHouseCode(), myPageDao.getFileList(list.getGuestHouseCode()));

		}

		model.addAttribute("fileMap", map);
		model.addAttribute("likeList", myPageDao.myLike(cri, memberCode));
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(myPageDao.getLikeTotal(memberCode));

		model.addAttribute("pageMaker", pageMaker);

	}

	// 찜 삭제
	@RequestMapping("/deleteLike")
	public String deleteLike(HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
		int memberCode = ((Integer) session.getAttribute("memberCode")).intValue();
		String[] arr = request.getParameterValues("chk");
		List<String> list = Arrays.asList(arr);
		List<Integer> deleteLikeList = list.stream().map(Integer::parseInt).collect(Collectors.toList());
		myPageDao.deleteLike(memberCode, deleteLikeList);
		redirectAttributes.addFlashAttribute("deleteOk", "선택한 찜이 삭제되었습니다.");
		return "redirect:/myPage/myLike";
	}
}
