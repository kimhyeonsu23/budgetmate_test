package com.budgetmate.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.budgetmate.dto.HistoryDto;
import com.budgetmate.security.TokenParser;
import com.budgetmate.service.HistoryService;

@RestController
@RequestMapping("/history")
public class HistoryController {
	
	private final HistoryService historyService;
	private final TokenParser tokenParser;
	
	public HistoryController(HistoryService historyService, TokenParser tokenParser) {
		System.out.println("컨트롤러 생성자 시작");
		this.historyService = historyService;
		this.tokenParser  = tokenParser;
	}
	
	@PostMapping("/getGrantedDate") // 뱃지 날짜를 가져오기. (뱃지 유무는 그냥 유저 서비스에서 badge 속성으로 확인할 것)
	public ResponseEntity<List<HistoryDto>> getHistoryDate(@RequestHeader("Authorization") String authHeader) { // 유저 아이디랑 뱃 
		// Long : badgeId, LocalDateTime
		System.out.println("1 : 컨트롤러 실행");
		String token = authHeader.replace("Bearer ", "").trim();
		Long userId = tokenParser.getUserIdFromToken(token);
		return ResponseEntity.ok(historyService.getHistoryDate(userId));
		// List<HistoryDto>형태로 넘겨줘도 프론트에서는 일반 json배열로 받기만 할 수 있음, dto는 백엔드에서만 필요한 개념.
	}

}
// List<Dto> 와 List<Map<String,,>> 은 완전히 다른 개념임. 동적타입 vs 정적타입 => 그러나 반환 값은 똑같아 보임.
