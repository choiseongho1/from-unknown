package com.fromunknown.letter.service;

import com.fromunknown.letter.dto.LetterResponseDto;
import com.fromunknown.letter.entity.Letter;
import com.fromunknown.letter.repository.LetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;

    public void save(String message) {
        Letter letter = Letter.create(message);
        letterRepository.save(letter);
    }

    public LetterResponseDto receiveRandomLetter() {
        return letterRepository.findTopByIsDeliveredTrueAndIsConsumedFalse()
                .map(letter -> {
                    letter.markConsumed();
                    letterRepository.save(letter);
                    return LetterResponseDto.fromEntity(letter.getMessage(), LocalDateTime.now());
                })
                .orElseThrow(() -> new IllegalStateException("오늘 도착한 편지가 없어요 😢"));
    }
}
