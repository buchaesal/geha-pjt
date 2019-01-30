package com.bit.geha.controller;

import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.dao.MemberDao;
import com.bit.geha.dto.MemberDto;
import com.bit.geha.service.MemberService;


@Controller
@RequestMapping("/member")
public class MemberController {

	@Autowired
	MemberDao memberDao;
	
	@Autowired
	MemberService memberService;
	

	@RequestMapping("/login")
	public void login() {
	}
	
	@RequestMapping("/findPw")
	public void findPw() {
	}
	
	@RequestMapping("/signUp")
	public void signUp() {
	}
	
	@RequestMapping("/hostSignUp")
	public String hostSignUp(Model model) {
		model.addAttribute("host","host");
		return "/member/signUp";
	}
	
	@RequestMapping("/changePw")
	public void changePw() {
	}
	
	@RequestMapping("/sendEmailComplete")
	public void sendEmailComplete() {
	}
	
	@RequestMapping("/chooseAuth")
	public void chooseAuth() {
	}
	
	@PostMapping(value="findPw.do")
	@ResponseBody
	public String findPw(@RequestBody String id) throws Exception {
		
		return memberService.findPw(id);
		
	}

	@PostMapping(value = "/idcheck.do")
	@ResponseBody
	public Map<Object, Object> idcheck(@RequestBody String id) {

		int count = 0;
		Map<Object, Object> map = new HashMap<Object, Object>();

		count = memberDao.idCheck(id);
		map.put("cnt", count);

		return map;
	}

	@PostMapping("/create")
	public String create(MemberDto memberDto) throws Exception {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
		//memberDto.setAuthority("USER");
		memberService.save(memberDto);
		
		return "/member/sendEmailComplete";
		
	}
	
	@RequestMapping(value = "/emailConfirming", method = RequestMethod.GET)
	public String emailConfirming(String id,String key,Model model) 
			throws Exception { // 이메일인증
		
		MemberDto memberDto= memberDao.findById(id);
		if(key.equals(memberDto.getAuthCode())) {
			memberDao.userAuth(id);
			model.addAttribute("name", memberDto.getMemberName());
		}else {
			model.addAttribute("error", "인증에 실패했습니다. 다시 시도해주세요.");
		}
		
		return "/member/emailConfirm";
	}
	
	@RequestMapping("/emailConfirm")
	public void emailConfirm() {
	
	}
}
