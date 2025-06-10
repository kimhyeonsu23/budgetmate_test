package com.budgetmate.query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.budgetmate.dto.HistoryDto;


@Repository // 스프링이 이 클래스를 데이터 접근 계층으로 인식하도록 해야하고 자동으로 빈에 등록해줌.
public class HistoryQuery {
	
	private final JdbcTemplate jdbcTemplate;
	
	public HistoryQuery(JdbcTemplate jdbcTemplate) {
		
		this.jdbcTemplate = jdbcTemplate;
		
	}

//	String sql = "select * from user";
//	List<UserDto> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserDto.class));
////User.class를 넘기는 이유는 ResultSet을 User라는 클래스에 매핑할것이라고 jdbcTemplate에게 알려줌 -> user.class를 넘기면 내부적으로 reflection을 사용해서 setter를 자동으로 실행.
//	return users;
	
	public List<HistoryDto> getHistoryDate(Long userId) {
		
		System.out.println("3 : 쿼리 실행");
		System.out.println("userId = " + userId);
		String sql = "SELECT badge_id, granted_date FROM history WHERE user_id = ?";
		
		//List<Map<String, Object>> historyDateList = jdbcTemplate.queryForList(sql, userId);
// jdbcTemplate.queryForList()는 무조건 List<Map<String, Object>>로 반환함. 다른 타입이 지정된 형태로는 받을 수 없음.
// 왜냐하면 queryForList()는 내부적으로 컬럼명을 String : key, object : vale로 매핑함.
		List<HistoryDto> historyList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<> (HistoryDto.class), userId);
		return historyList;
		
		
	}

}
