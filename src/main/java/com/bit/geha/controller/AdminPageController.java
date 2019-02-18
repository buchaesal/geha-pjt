package com.bit.geha.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bit.geha.criteria.AdminPageCriteria;
import com.bit.geha.dao.AdminPageDao;
import com.bit.geha.dao.RoomDao;
import com.bit.geha.dto.FacilityDto;
import com.bit.geha.dto.FileDto;
import com.bit.geha.dto.GuestHouseDto;
import com.bit.geha.dto.MemberDto;
import com.bit.geha.dto.RejectDto;
import com.bit.geha.dto.RoomDto;
import com.bit.geha.service.MemberService;
import com.bit.geha.util.PageMaker;

@Controller
@RequestMapping("/adminPage")
public class AdminPageController {

	@Autowired
	AdminPageDao adminPageDao;

	@Autowired
	MemberService memberService;

	@Autowired
	RoomDao roomDao;

	@GetMapping("/adminPage")
	public void adminPage(@ModelAttribute("cri") AdminPageCriteria cri, String auth, HttpSession session,
			Authentication authority, Model model) {
		memberService.getSession(authority, session);
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);

		if (!auth.equals("all")) {
			model.addAttribute("list", adminPageDao.getMemberList(cri, auth));
			model.addAttribute("auth", auth);
			pageMaker.setTotalCount(adminPageDao.getTotal(cri, auth));
		} else {
			model.addAttribute("list", adminPageDao.getMemberList(cri, ""));
			model.addAttribute("auth", "");
			pageMaker.setTotalCount(adminPageDao.getTotal(cri, ""));
		}

		model.addAttribute("pageMaker", pageMaker);

	}

	@RequestMapping("/changeAdmin")
	public String changeAdmin(@RequestParam("id") List<String> id, RedirectAttributes redirectAttributes,
			@RequestParam("auth") String auth) {
		adminPageDao.changeAdmin(id);
		redirectAttributes.addFlashAttribute("changeAdmin", "선택한 회원을 관리자로 임명하였습니다");
		return "redirect:/adminPage/adminPage?auth=" + auth;
	}

	@RequestMapping("/changeUser")
	public String changeUser(@RequestParam("id") List<String> id, RedirectAttributes redirectAttributes,
			@RequestParam("auth") String auth) {
		adminPageDao.changeUser(id);
		redirectAttributes.addFlashAttribute("changeUser", "선택한 관리자를 일반회원으로 강등하였습니다.");
		return "redirect:/adminPage/adminPage?auth=" + auth;
	}

	@RequestMapping("/approvalHouse")
	public void approvalHouse(Model model, @ModelAttribute("cri") AdminPageCriteria cri) {
		cri.setPerPageNum(5);

		// 페이징
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(adminPageDao.getApprovalTotal());
		
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("list", adminPageDao.getApprovalHouseList(cri));
		model.addAttribute("tomorrow",memberService.getTomorrow());
	}

	@RequestMapping("getMemberInfo.do")
	@ResponseBody
	public List<MemberDto> getMemberInfo(@RequestBody int memberCode) {

		return memberService.findByMemberCode(memberCode);
	}

	// 승인,반려 할수있는 뷰 페이지
	@RequestMapping("/waitApproval")
	public void waitApproval(@RequestParam("bookingStart") String bookingStart,
			@RequestParam("bookingEnd") String bookingEnd, @RequestParam("bookingNumber") int bookingNumber,
			@RequestParam("guestHouseCode") int guestHouseCode, Model model) throws Exception {
		List<RoomDto> rooms = roomDao.roomInfo(bookingStart, bookingEnd, bookingNumber, guestHouseCode);
		List<FacilityDto> facility = roomDao.facilityInfo(guestHouseCode);
		List<FileDto> gehaImg = roomDao.gehaImg(guestHouseCode);
		
		GuestHouseDto guestHouseDto = roomDao.gehaInfo(guestHouseCode);
		model.addAttribute("gehaImg", gehaImg);
		model.addAttribute("guestHouseCode", guestHouseCode);
		model.addAttribute("memberCode", guestHouseDto.getMemberCode());
		model.addAttribute("geha", guestHouseDto);
		model.addAttribute("room", rooms);
		model.addAttribute("facility", facility);

	}

	@RequestMapping("/approveGuestHouse")
	public String approveGuestHouse(@RequestParam("guestHouseCode") int guestHouseCode,
			RedirectAttributes redirectAttributes) {
		adminPageDao.approveNewGuestHouse(guestHouseCode);
		redirectAttributes.addFlashAttribute("approveOk", "승인이 성공적으로 완료되었습니다.");
		return "redirect:/adminPage/waitApproval?guestHouseCode=" + guestHouseCode;
	}

	@RequestMapping("/rejectGuestHouse")
	public String rejectGuestHouse(RejectDto rejectDto, RedirectAttributes redirectAttributes) {

		adminPageDao.rejectNewGuestHouse(rejectDto.getGuestHouseCode());
		adminPageDao.insertReject(rejectDto);
		redirectAttributes.addFlashAttribute("approveOk", "반려가 처리되었습니다.");
		return "redirect:/adminPage/waitApproval?guestHouseCode=" + rejectDto.getMemberCode();
	}

	@RequestMapping("getRejectList.do")
	@ResponseBody
	public List<RejectDto> getRejectList(@RequestBody int guestHouseCode) {

		return adminPageDao.getRejectListByGuestHouseCode(guestHouseCode);
	}
}
