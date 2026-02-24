package com.example.join.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//=스프링 설정 파일이다 선언.
@Configuration 
public class SecurityConfig {
	//@Bean = 메서드가 리턴하는 객체를 스프링이 관리하게 등록.
	@Bean 
	public PasswordEncoder passwordEncoder() {
		//비밀번호를 단방향(암호화O, 복호화X) 해시바꾸는 알고리즘 (BCrypt)
		return new BCryptPasswordEncoder();
	}
	//Spring Security 추가하면 = 모든 페이지 로그인 강제.
	//자동보안 기능 끄기.(안끄면 모든 페이지 접속시 로그인 페이지가 뜸)
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
			//csrf(다른사이트에서 요청보내는 공격) 보호 끄기(안끄면post요청 다 막힘)
			.csrf(crsf -> crsf.disable())
			//모든 url에 대한 접근 허용.(현재 로그인체크 = 컨트롤러에서 직접 하고있음)
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll()
				)
			//security가 제공하는 기본로그인(끄기) 대신 user-login.html사용.
			.formLogin(form -> form.disable())
			//security가 제공하는 기본로그인 폼(끄기) 대신user-login.html사용.
			.logout(logout -> logout.disable())
			//H2 콘솔은 iframe으로 동작.->security가 iframe차단.
			//->아래 설정으로 iframe 차단을 해제.
			.headers(headers -> headers
				.frameOptions(frame -> frame.disable())
			);
		return http.build();
	}
	
}
