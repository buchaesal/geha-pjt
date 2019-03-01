package com.bit.geha.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bit.geha.dao.MemberDao;
import com.bit.geha.dto.MemberDto;
import com.bit.geha.security.SecurityMember;
import com.bit.geha.util.TempKey;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private JavaMailSender mailSender;

	@Transactional
	public void save(MemberDto memberDto) throws Exception {
		memberDao.insertUser(memberDto); // 정보 데이터에 넣기
		// memberMapper.insertUserAutority(member.getAuthorityCode(), authority);

		String key = new TempKey().getKey(50, false); // 인증키 생성

		memberDao.createAuthKey(memberDto.getId(), key); // 인증키 DB저장

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8"); // 두번째 인자 true여야 파일첨부 가능.

			messageHelper.setFrom("eksqlrhdwb@naver.com", "#GEHA운영자"); // 보내는사람 생략하거나 하면 정상작동을 안함
			messageHelper.setTo(memberDto.getId()); // 받는사람 이메일
			messageHelper.setSubject("[#GEHA 회원가입 이메일 인증]"); // 메일제목은 생략이 가능하다
			messageHelper.setText(new StringBuffer().append("<h1>회원가입 인증 메일입니다.</h1><h3>아래의 버튼을 눌러 회원가입을 완료해주세요.</h3>")
					.append("<a href='http://geha.tk/member/emailConfirming?id=").append(memberDto.getId())
					.append("&key=").append(key).append("' target='_blank'>회원가입 인증 확인</a>").toString(), true); // 메일 내용

			mailSender.send(message);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void sendMail(String id) throws Exception {
		MemberDto member = memberDao.findById(id);

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8"); // 두번째 인자 true여야 파일첨부 가능.

			messageHelper.setFrom("eksqlrhdwb@naver.com", "#GEHA운영자"); // 보내는사람 생략하거나 하면 정상작동을 안함
			messageHelper.setTo(id); // 받는사람 이메일
			messageHelper.setSubject("[#GEHA 회원가입 이메일 인증]"); // 메일제목은 생략이 가능하다
			messageHelper.setText(new StringBuffer().append("<h1>회원가입 인증 메일입니다.</h1><h3>아래의 버튼을 눌러 회원가입을 완료해주세요.</h3>")
					.append("<a href='http://geha.tk/member/emailConfirming?id=").append(id).append("&key=")
					.append(member.getAuthCode()).append("' target='_blank'>회원가입 인증 확인</a>").toString(), true); // 메일 내용

			mailSender.send(message);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public String findPw(String id) throws Exception {

		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

		int idx = 0;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < 10; i++) {
			idx = (int) (charSet.length * Math.random());
			sb.append(charSet[idx]);
		}

		String text = sb.toString();

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8"); // 두번째 인자 true여야 파일첨부 가능.

			messageHelper.setFrom("eksqlrhdwb@naver.com", "#GEHA운영자"); // 보내는사람 생략하거나 하면 정상작동을 안함
			messageHelper.setTo(id); // 받는사람 이메일
			messageHelper.setSubject("[#GEHA] 비밀번호 찾기 인증코드 발신 메일입니다."); // 메일제목은 생략이 가능하다
			messageHelper.setText(
					new StringBuffer().append("<h1>비밀번호 찾기 인증코드입니다.</h1><h3>아래의 인증코드를 비밀번호 찾기 페이지에서 인증해주세요.</h3>")
							.append("인증코드 : " + text).toString(),
					true); // 메일 내용

			mailSender.send(message);

		} catch (Exception e) {
			System.out.println(e);
		}

		return text;
	}

	// 사용자의 정보를 세션에 넣어줌.(로그인 후 접근 할 가능성이 있는 페이지에 전부 넣어준다.)
	public void getSession(Authentication auth, HttpSession session) {

		if (auth != null && session.getAttribute("name") == null) {

			SecurityMember sc = (SecurityMember) auth.getPrincipal();
			MemberDto member = memberDao.findById(sc.getUsername());
			session.setAttribute("name", member.getMemberName());
			session.setAttribute("auth", member.getAuthority());
			session.setAttribute("memberCode", member.getMemberCode());

		}

	}

	public List<MemberDto> findByMemberCode(int memberCode) {
		return memberDao.findByMemberCode(memberCode);
	}

	public String getTomorrow() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1); // 하루를 더해준다.
		return sdf.format(c.getTime());
	}
}
