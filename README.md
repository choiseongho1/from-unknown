
# 📮 우주편지 앱 API 연동 가이드 – *From Unknown*

> “당신이 알지 못하는 누군가에게서, 편지가 도착했습니다.”

---

## ✅ 공통 정보

| 항목 | 설명 |
|------|------|
| Base URL | `http://localhost:8080` (로컬) or `https://fromunknown.app` (배포 시) |
| 인증 | 없음 (익명 쿠키 기반) |
| 쿠키 | 서버에서 `client-token` 자동 발급 → 클라이언트 저장 필요 |
| 응답 포맷 | JSON (UTF-8) |

---

## 💌 1. 편지 작성

### ▶️ `POST /api/letters`

#### 요청

```http
POST /api/letters
Content-Type: application/json
Cookie: client-token=abc123 (생략 가능)

Body:
"요즘은 밤하늘을 보면 조금 괜찮아지는 것 같아요."
```

#### 응답

- ✅ `200 OK`: 작성 성공, 서버가 `Set-Cookie: client-token=...` 쿠키 발급
- ❌ `429 TOO MANY REQUESTS`: 하루 1통 제한 메시지

```json
"오늘은 이미 편지를 보냈어요. 내일 다시 찾아와 주세요 🕊️"
```

#### 💡 UX 팁
- 첫 요청 시 쿠키 자동 저장 필요 (Flutter에서는 `http` 라이브러리 + 쿠키 핸들링)
- 제한 메시지일 경우 감성 문구로 변경해 보여줘도 좋음

---

## 📬 2. 편지 받기

### ▶️ `GET /api/letters/today`

#### 요청

```http
GET /api/letters/today
Cookie: client-token=abc123
```

#### 응답

- ✅ `200 OK`

```json
{
  "message": "때로는 아무도 모르게 힘들 수 있어요.\n하지만 어딘가에서 누군가는 응원하고 있을지도 몰라요.",
  "from": "From Unknown",
  "receivedAt": "2025-04-15T13:30:00"
}
```

- ❌ `500`: 아직 도착한 편지가 없어요

```json
{
  "message": "오늘 도착한 편지가 없어요 😢"
}
```

#### 💡 UX 팁
- 편지를 읽은 뒤엔 다시 조회할 수 없음 (단 1회 노출)
- 편지 도착 효과 (✨ + 애니메이션) 넣어주면 몰입감 상승
- "편지가 도착하지 않았어요" 경우, 조용한 우주 배경과 함께 표시

---

## 🍪 쿠키 처리 흐름 (중요)

| 시나리오 | 처리 |
|----------|------|
| 최초 요청 | 서버가 `Set-Cookie: client-token` 응답 → 클라이언트 저장 |
| 이후 요청 | 자동으로 쿠키 포함 |
| 삭제 시 | 새로운 UUID 발급됨 (우회 방지 위해 IP 조합 포함됨) |

---

## 📱 Flutter 연동 팁

- `http` + `cookie_jar` 또는 `dio` + `cookie_manager` 조합 추천
- 처음 편지 작성 시 서버에서 `Set-Cookie` 확인해 클라이언트에 저장
- 이후 요청 시 자동으로 쿠키 전달되도록 설정

---

## 🔒 참고: 하루 1통 제한 키 구성

| 항목 | Redis 키 예시 |
|------|---------------|
| 쿠키 토큰 | `letter:sent:abc123:192.168.0.1` |
| TTL | 24시간 |

---

## ✅ 요약: 전체 흐름

```
[앱 첫 접속]
 ↓
[편지 작성 → 서버가 쿠키 발급 → Redis로 1일 제한]
 ↓
[배달 시간이 되면 스케줄러가 isDelivered=true 처리]
 ↓
[앱 접속 시 1통의 편지 도착 → 읽으면 소멸]
```