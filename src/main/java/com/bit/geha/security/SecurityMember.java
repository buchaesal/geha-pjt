package com.bit.geha.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.bit.geha.dto.MemberDto;



public class SecurityMember extends User implements UserDetails {
	private static final String ROLE_PREFIX = "ROLE_";
	private static final long serialVersionUID = 1L;
	private int memberCode;
	private String authority;
	private String memberName;
	
	public SecurityMember(MemberDto member){
		super(member.getId(),member.getPassword(),makeGrantedAuthority(member));
		this.memberCode=member.getMemberCode();
		this.authority=member.getAuthority();
		this.memberName=member.getMemberName();
	}
	
	private static List<GrantedAuthority> makeGrantedAuthority(MemberDto member){
		List<GrantedAuthority> list = new ArrayList<>();
		list.add(new SimpleGrantedAuthority(ROLE_PREFIX + member.getAuthority()));
		return list;
	}

	

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public int getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(int memberCode) {
		this.memberCode = memberCode;
	}
	
	
}
