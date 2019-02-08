package com.bit.geha.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bit.geha.dao.MemberDao;
import com.bit.geha.dto.MemberDto;
import com.bit.geha.security.SecurityMember;
import com.bit.geha.util.MailHandler;
import com.bit.geha.util.TempKey;


@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Transactional
	public void save(MemberDto memberDto) throws Exception {
		memberDao.insertUser(memberDto); //정보 데이터에 넣기
		//memberMapper.insertUserAutority(member.getAuthorityCode(), authority);
		
		String key = new TempKey().getKey(50, false); // 인증키 생성

		memberDao.createAuthKey(memberDto.getId(), key); // 인증키 DB저장

		MailHandler sendMail = new MailHandler(mailSender);
		sendMail.setSubject("[#GEHA 회원가입 이메일 인증]");
		sendMail.setText(
				new StringBuffer()
				.append("<h1>회원가입 인증 메일입니다.</h1><h3>아래의 버튼을 눌러 회원가입을 완료해주세요.</h3>")
				.append("<a href='http://localhost/member/emailConfirming?id=")
				.append(memberDto.getId()).append("&key=").append(key)
				.append("' target='_blank'>회원가입 인증 확인</a>").toString());
		sendMail.setFrom("eks4116@gmail.com", "샵게하 운영자");
		sendMail.setTo(memberDto.getId());
		sendMail.send();
		
	}
	
	public void sendMail(String id) throws Exception {
			MemberDto member = memberDao.findById(id);
			MailHandler sendMail = new MailHandler(mailSender);
			sendMail.setSubject("[#GEHA 회원가입 이메일 인증]");
			sendMail.setText(
					new StringBuffer()
					.append("<h1>회원가입 인증 메일입니다.</h1><h3>아래의 버튼을 눌러 회원가입을 완료해주세요.</h3>")
					.append("<a href='http://localhost/member/emailConfirming?id=")
					.append(id).append("&key=").append(member.getAuthCode())
					.append("' target='_blank'>회원가입 인증 확인</a>").toString());
			sendMail.setFrom("eks4116@gmail.com", "샵게하 운영자");
			sendMail.setTo(id);
			sendMail.send();
		
	}
	
	public String findPw(String id) throws Exception {
		
		
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
				'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		
		int idx = 0;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < 10; i++) { 
			idx = (int) (charSet.length * Math.random()); 
			 sb.append(charSet[idx]);
		}
		
		String text=sb.toString();
		
		MailHandler sendMail = new MailHandler(mailSender);
		sendMail.setSubject("[#GEHA] 비밀번호 찾기 인증코드 발신 메일입니다. ");
		sendMail.setText("인증코드 : " + text);
		sendMail.setText(
				new StringBuffer()
				.append("<h1>비밀번호 찾기 인증코드입니다.</h1><h3>아래의 인증코드를 비밀번호 찾기 페이지에서 인증해주세요.</h3>")
				.append("인증코드 : " + text).toString());
		sendMail.setFrom("eks4116@gmail.com", "샵게하 운영자");
		sendMail.setTo(id);
		sendMail.send();
		
		return text;
	}

	public void getSession(Authentication auth,HttpSession session) {
		
		
		if (auth != null && session.getAttribute("name")==null )  { 

			SecurityMember sc = (SecurityMember) auth.getPrincipal();
			MemberDto member = memberDao.findById(sc.getUsername());
			session.setAttribute("name", member.getMemberName());
			session.setAttribute("auth", member.getAuthority());
			session.setAttribute("memberCode", member.getMemberCode());
		}
		
	}

}
