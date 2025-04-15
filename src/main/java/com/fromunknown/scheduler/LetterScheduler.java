package com.fromunknown.scheduler;

import com.fromunknown.letter.entity.Letter;
import com.fromunknown.letter.repository.LetterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LetterScheduler {

    private final LetterRepository letterRepository;

    // 5분마다 실행 (초, 분, 시)
    @Scheduled(fixedDelay = 10000)
    public void deliverScheduledLetters() {
        LocalDateTime now = LocalDateTime.now();

        List<Letter> toDeliver = letterRepository
                .findAll()
                .stream()
                .filter(letter -> !letter.isDelivered() && letter.getDeliverAt().isBefore(now))
                .toList();

        if (!toDeliver.isEmpty()) {
            toDeliver.forEach(Letter::markDelivered);
            letterRepository.saveAll(toDeliver);
            log.info("📬 {}개의 편지를 배달 완료했습니다.", toDeliver.size());
        }
    }
}
