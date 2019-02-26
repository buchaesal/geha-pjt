package com.bit.geha.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping("/login")
	public void login(HttpServletRequest request) {

		String referrer = request.getHeader("Referer");
		request.getSession().setAttribute("prevPage", referrer);
	}

	@PostMapping(value = "/loginCk")
	@ResponseBody
	public String loginCk(@RequestParam("id") String id, @RequestParam("password") String password) { // 로그인 유효성검사

		MemberDto member = memberDao.findById(id);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		if (member != null) { // 아이디가 존재한다면
			if (passwordEncoder.matches(password, member.getPassword())) { // 비밀번호도 일치한다면

				if (member.getAuthStatus() != null) { // 가입 할 때 메일인증도 받은 사람이라면!!!!
					return "";
				} else { // 메일인증은 안받았다면
					return "노인증";
				}
			} else {// 일치하지 않는다면
				return "비밀번호가 일치하지 않습니다.";
			}
		} else {// 아이디가 존재하지 않는다면
			return "아이디가 존재하지 않습니다.";
		}

	}

	@RequestMapping("/findPw")
	public void findPw() {
	}

	@RequestMapping("/signUp")
	public void signUp() {
	}

	@RequestMapping("/hostSignUp")
	public String hostSignUp(Model model) {
		model.addAttribute("host", "host");
		return "/member/signUp";
	}

	@RequestMapping("/changePw")
	public void changePw(Model model, @RequestParam("hiddenId") String id) {
		model.addAttribute("id", id);
	}

	@RequestMapping("/sendEmailComplete")
	public void sendEmailComplete() {
	}

	@PostMapping(value = "findPw.do")
	@ResponseBody
	public String findPw(@RequestBody String id) throws Exception {
		id = id.replace("%40", "@").substring(3);
		if (memberDao.findById(id) == null) { // 그 이메일을 가진 회원이 존재하지 않는다면
			return "";
		} else { // 존재한다면 인증코드 메일을 보낸다
			return memberService.findPw(id);
		}
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
		memberService.save(memberDto);

		return "/member/sendEmailComplete";

	}

	@RequestMapping(value = "/emailConfirming", method = RequestMethod.GET)
	public String emailConfirming(String id, String key, Model model) throws Exception { // 이메일인증

		MemberDto memberDto = memberDao.findById(id);
		if (key.equals(memberDto.getAuthCode())) {
			memberDao.userAuth(id);
			model.addAttribute("name", memberDto.getMemberName());
		} else {
			model.addAttribute("error", "인증에 실패했습니다. 다시 시도해주세요.");
		}

		return "/member/emailConfirm";
	}

	@RequestMapping("/emailConfirm")
	public void emailConfirm() {

	}

	@RequestMapping("sendMail.do")
	@ResponseBody
	public void sendMail(@RequestParam(value = "id") String id) throws Exception {
		memberService.sendMail(id);
	}

	@RequestMapping("updatePw.do")
	@ResponseBody
	public void updatePw(@RequestParam(value = "password") String password, @RequestParam(value = "id") String id) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		memberDao.changePw(id, passwordEncoder.encode(password));

	}
}
