package com.fromunknown.letter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LetterResponseDto {

    @Schema(description = "편지 내용", example = "당신의 하루가 무사했기를 바라요.")
    private String message;

    @Schema(description = "발신자 (항상 'From Unknown')", example = "From Unknown")
    private String from;

    @Schema(description = "편지를 받은 시각", example = "2025-04-15T12:00:00")
    private LocalDateTime receivedAt;

    public static LetterResponseDto fromEntity(String message, LocalDateTime receivedAt) {
        return LetterResponseDto.builder()
                .message(message)
                .from("From Unknown")
                .receivedAt(receivedAt)
                .build();
    }
}
