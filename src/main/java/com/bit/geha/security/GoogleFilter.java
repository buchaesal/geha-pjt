package com.bit.geha.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.bit.geha.dto.MemberDto;
import com.bit.geha.service.SocialService;

public class GoogleFilter extends OAuth2ClientAuthenticationProcessingFilter {
	
	
	private SocialService service;

	
	public GoogleFilter(SocialService service) {
		super("/login/google");
		this.service=service;
	}
	 
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		
		final OAuth2AccessToken accessToken = restTemplate.getAccessToken(); // 토큰 정보 가져옴
        final OAuth2Authentication auth = (OAuth2Authentication) authResult;
        final Map<String, String> map = (HashMap<String, String>)auth
        		.getUserAuthentication().getDetails(); // 소셜에서 넘겨 받은 정보를 details에 저장
        MemberDto memberDto = new MemberDto();
        memberDto.setAuthority("USER");
        memberDto.setAuthStatus("OK");
        memberDto.setId(map.get("email"));
        memberDto.setMemberName(map.get("name"));
        memberDto.setPassword("socialMember");
       
        final UsernamePasswordAuthenticationToken authenticationToken = 
        		service.doAuthentication(memberDto); // SocialService를 이용해서 인증 절차 진행
        
        super.successfulAuthentication(request, response, chain, authenticationToken);
        
	}
	
	
}
