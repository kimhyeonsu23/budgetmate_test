package com.budgetmate.security;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component	// spring의 빈으로 등록되도록 함.
public class TokenParser {
	
	//private final String secretKey = "w9vM9r6ZKLEFh82N0UbpVYkRIuv2AfxN"; // 토큰을 만들 때와 같은 비밀 키.
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	public Long getUserIdFromToken(String token) {
		
		System.out.println("이것은 token parser 입니다.");
		Claims claims = Jwts.parser() // jwt parser를 만드는 진입점. 파서 객체를 구성. 
				.verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
				// verifyWith() : jwt 서명을 검증할 키를 설정하는 부분.
				//secretKey.getButes(StandardCharsets.UTF_8) : 문자열 키를 바이트 배열로 변환함. 9utf-8인코딩을 사용)
				// Keys.hamcShaKeyFor() : HMAC-SHA 알고리즘을 사용할 수 있는 적절한 secretKey객체를 생성함. 
				.build() // 파서 객체를 빌드하는 단계.
				.parseSignedClaims(token)   // token문자열을 실제로 파싱하는 부분. jwt 서명이 검증되고 실패하면 예외 발생. jwt의 클레임을 포함한 객체 Jws<Claims>를 반환.
				.getBody(); // jwt의 payload 영역을 의미. 즉 토큰 내부의 userId, ... 등 클레임을 가져오는 부분. 
		
		return claims.get("id", Long.class);
	}

}
