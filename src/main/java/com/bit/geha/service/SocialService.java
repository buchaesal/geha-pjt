package com.bit.geha.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.bit.geha.dao.MemberDao;
import com.bit.geha.dto.MemberDto;
import com.bit.geha.security.SecurityMember;

import lombok.AllArgsConstructor;
@Component
@AllArgsConstructor
public class SocialService {
	MemberDao memberDao;
	
	public UsernamePasswordAuthenticationToken doAuthentication(MemberDto memberDto) {
		MemberDto member = memberDao.findById(memberDto.getId());
		if (member!=null) {
	        // 기존 회원일 경우에는 데이터베이스에서 조회해서 인증 처리
	        final SecurityMember user = new SecurityMember(member);
	        return setAuthenticationToken(user);
	    } else {
	        // 새 회원일 경우 회원가입 이후 인증 처리
	    	memberDto.setAuthority("USER");
	        memberDto.setAuthStatus("OK");
	        memberDto.setPassword("socialMember");
	        memberDto.setGender("M");
	    	memberDao.insertUser(memberDto);
	    	memberDao.userAuth(memberDto.getId());
	    	
	        final SecurityMember user = new SecurityMember(memberDto);
	        
	        
	      
	        return setAuthenticationToken(user);
	    }
	}
	
	 private UsernamePasswordAuthenticationToken setAuthenticationToken(SecurityMember user) {
	        return new UsernamePasswordAuthenticationToken(user,
	        		null, getAuthorities("ROLE_"+user.getAuthority()));
	    }
	 
	 private Collection<? extends GrantedAuthority> getAuthorities(String role) {
	        List<GrantedAuthority> authorities = new ArrayList<>();
	        authorities.add(new SimpleGrantedAuthority(role));
	        return authorities;
	    }
}
