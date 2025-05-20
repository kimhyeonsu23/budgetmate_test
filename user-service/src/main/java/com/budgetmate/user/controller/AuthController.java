package com.budgetmate.user.controller;

import com.budgetmate.user.dto.LoginRequest;
import com.budgetmate.user.dto.SignupRequest;
import com.budgetmate.user.entity.User;
import com.budgetmate.user.security.CustomUserDetails;
import com.budgetmate.user.service.EmailService;
import com.budgetmate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;

    // 이메일 -> 인증코드 + 생성시각 저장
    private final Map<String, VerificationInfo> verificationCodes = new ConcurrentHashMap<>();
    private static final long CODE_EXPIRE_TIME = 5 * 60 * 1000; // 5분

    //  인증코드 발송
    //  인증코드 발송
    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        log.info("[AuthController] /send-code 요청 들어옴 → {}", email);

        // 이미 가입된 이메일인지 확인
        if (userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "이미 가입된 이메일입니다."
            ));
        }

        String code = emailService.sendVerificationCode(email);
        verificationCodes.put(email, new VerificationInfo(code));  // ✅ 여기를 이렇게!
        return ResponseEntity.ok(Map.of("success", true, "message", "이메일 전송 완료"));
    }


    //  인증코드 확인
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String inputCode = request.get("code");

        VerificationInfo info = verificationCodes.get(email);
        if (info == null) {
            return ResponseEntity.badRequest().body(Map.of("verified", false, "message", "인증 요청 내역이 없습니다."));
        }

        if (info.isExpired(CODE_EXPIRE_TIME)) {
            verificationCodes.remove(email);
            return ResponseEntity.badRequest().body(Map.of("verified", false, "message", "인증코드가 만료되었습니다."));
        }

        if (!info.getCode().equals(inputCode)) {
            return ResponseEntity.badRequest().body(Map.of("verified", false, "message", "인증코드가 일치하지 않습니다."));
        }

        verificationCodes.remove(email);
        return ResponseEntity.ok(Map.of("verified", true));
    }

    //  회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "이미 등록된 이메일입니다."
            ));
        }

        User newUser = userService.signup(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "user", newUser
        ));
    }


    //  로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }

    //   내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "userName", user.getUserName(),
                "roles", user.getRoles()
        ));
    }

    // 이메일 인증코드 + 생성시간 담는 클래스
    private static class VerificationInfo {
        private final String code;
        private final long createdAt;

        public VerificationInfo(String code) {
            this.code = code;
            this.createdAt = System.currentTimeMillis();
        }

        public boolean isExpired(long timeoutMillis) {
            return System.currentTimeMillis() - createdAt > timeoutMillis;
        }

        public String getCode() {
            return code;
        }
    }
}
