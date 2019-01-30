package com.bit.geha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bit.geha.dao.MemberDao;
import com.bit.geha.dto.MemberDto;
import com.bit.geha.security.SecurityMember;



//데이터베이스에서 등록된 사용자로 로그인되도록 설정하는 클래스
@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	MemberDao memberDao;

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

		MemberDto memberDto = memberDao.findById(id);
		if (memberDto != null) {
			return new SecurityMember(memberDto);
		} else {
			return null;
		}
	}
	
	
	


}
