package com.fromunknown.letter.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "letters")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Letter {

    @Id
    private String id;  // UUID 사용

    private String message;

    private LocalDateTime createdAt;

    private LocalDateTime deliverAt;

    private boolean isDelivered;

    private boolean isConsumed;

    public static Letter create(String message) {
        LocalDateTime now = LocalDateTime.now();
        return Letter.builder()
                .id(UUID.randomUUID().toString())
                .message(message)
                .createdAt(now)
                .deliverAt(randomFutureTime(now))
                .isDelivered(false)
                .isConsumed(false)
                .build();
    }

    private static LocalDateTime randomFutureTime(LocalDateTime from) {
        int randomHour = (int)(Math.random() * 72) + 1; // 1~72시간 사이
        return from.plusHours(randomHour);
    }

    public void markDelivered() {
        this.isDelivered = true;
    }

    public void markConsumed() {
        this.isConsumed = true;
    }
}
