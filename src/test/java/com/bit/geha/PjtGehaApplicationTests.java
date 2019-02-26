package com.bit.geha;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.bit.geha.dao.MemberDao;
import com.bit.geha.dao.MyPageDao;
import com.bit.geha.dto.MemberDto;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PjtGehaApplicationTests {
	
	@Autowired
	MemberDao memberDao;
	
	@Autowired
	MyPageDao myPageDao;

	@Test
	public void contextLoads() {
		
	/*	int memberCode = 248;
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		while(memberCode<261) {
			MemberDto member = memberDao.findMemberByMemberCode(memberCode);
			member.setPassword(passwordEncoder.encode("1234"));
			myPageDao.modifyInfo(memberCode,"abc"+memberCode+"@naver.com", member.getMemberName(), member.getPassword(), 
					member.getBusinessLicense(), memberCode>255?"F":"M");
			memberCode++;
		}*/
	}

}

