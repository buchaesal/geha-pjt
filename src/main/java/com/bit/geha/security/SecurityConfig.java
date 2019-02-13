package com.bit.geha.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CompositeFilter;

import com.bit.geha.service.SocialService;
import com.bit.geha.service.UserDetailService;



@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailService userDetailService;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	OAuth2ClientContext oauth2ClientContext;
	
	@Autowired
	SocialService socialService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/member/login","/member/findPw","/member/signUp",
					"/member/hostSignUp","/member/changePw","/member/chooseAuth",
					"/member/sendEmailComplete","/member/emailConfirm")
			.anonymous()
			.antMatchers("/myPage/*")
			.authenticated()
			.antMatchers("/**").permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/")
			.and()
			.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
		.formLogin()
			.loginPage("/member/login")
			.usernameParameter("id")
			.successHandler(successHandler())
			.and()
		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/").permitAll();
	}

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.eraseCredentials(false).userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
	    return new CustomLoginSuccessHandler("/");
	}
	
	 private Filter ssoFilter() {
	        CompositeFilter filter = new CompositeFilter();
	        List<Filter> filters = new ArrayList<>();
	        filters.add(ssoFilter(google(), new GoogleFilter(socialService))); //  이전에 등록했던 OAuth 리다이렉트 URL 
	        filters.add(ssoFilter(facebook(), new FacebookFilter(socialService)));
	        filter.setFilters(filters);
	        return filter;
	    }
	 
	    private Filter ssoFilter(ClientResources client, OAuth2ClientAuthenticationProcessingFilter filter) {
	        
	        OAuth2RestTemplate restTemplate = 
	        		new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
	        
	        filter.setRestTemplate(restTemplate);
	        
	        UserInfoTokenServices tokenServices = 
	        		new UserInfoTokenServices(client.getResource().getUserInfoUri(), client.getClient().getClientId());
	        
	        tokenServices.setRestTemplate(restTemplate);
	        
	        filter.setTokenServices(tokenServices);
	        
	        filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler() {
	        	private RequestCache requestCache = new HttpSessionRequestCache();
	            public void onAuthenticationSuccess(HttpServletRequest request, 
	            		HttpServletResponse response, Authentication authentication) 
	            				throws IOException, ServletException {
	                
	            	SavedRequest savedRequest = requestCache.getRequest(request, response);
	            	
	            	if (savedRequest == null) {
	            		HttpSession session = request.getSession();
	            		if (session != null) {
	            			String redirectUrl = (String) session.getAttribute("prevPage");
	            			
	            			if (redirectUrl != null) {
	            				session.removeAttribute("prevPage");
	            				getRedirectStrategy().sendRedirect(request, response, redirectUrl);

	            			} else {
	            				super.onAuthenticationSuccess(request, response, authentication);
	            			}
	            		} else {
	            			super.onAuthenticationSuccess(request, response, authentication);
	            		}
	            		return;
	            	}
	            		
	            		String targetUrlParameter = getTargetUrlParameter();
	                    if (isAlwaysUseDefaultTargetUrl()
	                            || (targetUrlParameter != null 
	                            && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
	                        requestCache.removeRequest(request, response);
	                        super.onAuthenticationSuccess(request, response, authentication);

	                        return;
	                    }
	                    
	                    clearAuthenticationAttributes(request);
	                    
	                 // Use the DefaultSavedRequest URL
	                    String targetUrl = savedRequest.getRedirectUrl();
	                    logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
	                    getRedirectStrategy().sendRedirect(request, response, targetUrl);

	            }
	            	
	            
	        });
	        
	        return filter;
	    }
	    
		@Bean
	    @ConfigurationProperties("facebook")
	    public ClientResources facebook() {
	        return new ClientResources();
	    }
		
	    @Bean
	    @ConfigurationProperties("google")
	    public ClientResources google() {
	        return new ClientResources();
	    }
	
	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(
			OAuth2ClientContextFilter filter) {
	  FilterRegistrationBean registration = new FilterRegistrationBean();
	  registration.setFilter(filter);
	  registration.setOrder(-100);
	  return registration;
	}

}
