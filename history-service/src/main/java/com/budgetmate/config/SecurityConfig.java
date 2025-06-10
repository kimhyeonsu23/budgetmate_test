package com.budgetmate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	
	@Bean 	// spring이 IOC 컨테이너에 등록
	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) { // ServerHttpSecurity : spring Security에서 제공하는 webFlux용 안 설정 빌더 클래스
		
		return http
				.csrf(csrf -> csrf.disable())	// CSRF 보호를 끄는 설정. REST API + JWT 기반에서는 인증이 필요없음 -> disable()
				// csrf : 로그인된 사용자의 구너한을 훔쳐서 몰래 요청 보내는 공격. 서버가 랜덤 토큰을 보내면 클라이언트가 요청에 토큰을 담아서 보냄 -> 서버가 확인하고 만약 토큰이 없으면 차단. (이것은 springsecurity에서 디폴트설정에서 사용함)
				//.cors(cors -> cors.disable())	// spring security의 내장 CORS 필터를 끄는 설정 (CorsWebFIlter를 별도로 등록해서 처리할 예정이므로)
				.authorizeExchange(			// 어떤 요청을 허용할지 / 차단할지를 정하는 부분. ==> cors와의 차이는 어디서 온 요청인지 / .authorizeExchange() : 무엇을 요청했는지.를 따짐.
						exchange // 람다 매개변수
						-> exchange    // exchange : 경로 조건 설정 객체
						.anyExchange().permitAll()	// 모든 요청을 허용하겠다는 뜻.  ==> 이 authorizeExchange는 권한 체크.
						)
				.build();   // 지금까지 작성한 http 설정을 종합해서 securityWebFilterChain객체를 생성함.
								// => 이후 spring Security 필터로 등록되어서 요청마다 작동함.
		
	}

}
