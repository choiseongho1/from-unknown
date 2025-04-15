package com.fromunknown.letter.controller;

import com.fromunknown.common.RedisService;
import com.fromunknown.letter.dto.LetterResponseDto;
import com.fromunknown.letter.service.LetterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/letters")
@RequiredArgsConstructor
@Tag(name = "📮 우주 편지", description = "익명의 감성 편지 API")
public class LetterController {

    private final LetterService letterService;

    @Operation(summary = "편지 쓰기", description = "익명으로 자유롭게 편지를 보낼 수 있어요")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "작성 성공")
    })
    @PostMapping
    public ResponseEntity<?> writeLetter(@RequestBody String message,
                                         @CookieValue(value = "client-token", required = false) String clientToken,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {

        // 1. 쿠키 없으면 새로 생성해서 내려보냄
        if (clientToken == null) {
            clientToken = UUID.randomUUID().toString();

            ResponseCookie cookie = ResponseCookie.from("client-token", clientToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(365))
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());
        }

        // 2. 편지 저장
        letterService.save(message);

        return ResponseEntity.ok().build();
    }


    @Operation(summary = "편지 받기", description = "당신을 위한 우주의 편지가 도착했을 수 있어요")
    @GetMapping("/today")
    public LetterResponseDto getTodayLetter() {
        return letterService.receiveRandomLetter();
    }
}
