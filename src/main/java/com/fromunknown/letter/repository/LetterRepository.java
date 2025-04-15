package com.fromunknown.letter.repository;

import com.fromunknown.letter.entity.Letter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LetterRepository extends MongoRepository<Letter, String> {

    // 읽히지 않은, 배달된 편지 중 랜덤 하나 (MongoDB에서 랜덤은 직접 구현 필요)
    Optional<Letter> findTopByIsDeliveredTrueAndIsConsumedFalse();
}