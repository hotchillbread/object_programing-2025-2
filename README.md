# LogTalk - AI 상담 앱

## 🚨 빌드 에러 해결

### 일반 빌드 에러 (파일명 문제 등)
**[QUICK_FIX.md](./QUICK_FIX.md)** 참고

```bash
chmod +x fix_build_error.sh && ./fix_build_error.sh
```

### Gradle 캐시 손상 에러
**에러 예시:** `metadata.bin (No such file or directory)`

**[GRADLE_CACHE_ERROR.md](./GRADLE_CACHE_ERROR.md)** 참고

```bash
chmod +x fix_all_build_issues.sh && ./fix_all_build_issues.sh
```

### 올인원 해결 (모든 빌드 문제)
```bash
cd /Users/na-gyeong/Desktop/object_programing-2025-2
chmod +x fix_all_build_issues.sh
./fix_all_build_issues.sh
```

---

## 프로젝트 구조

```
com.example.logtalk
├── core
│   ├── di             (Dependency Injection 설정)
│   ├── firebase       (미래 확장 시 Firebase 관련 모듈)
│   ├── network        (Retrofit 등 API 통신 설정)
│   └── utils          (공통 유틸리티 함수)
├── data
│   ├── local          (Room DB 관련: DAO, Database, Entity, TypeConverters) DB 구현
│   ├── remote         (API 인터페이스 및 데이터 소스)
│   └── repositoryImpl (Domain 계층 Repository의 실제 구현체)
├── domain
│   ├── model          (도메인 모델 정의)
│   ├── repository     (데이터 소스 추상화 인터페이스)
│   └── usecase        (비즈니스 로직 및 UseCase 정의)
└── ui
    ├── chat           (메시지 화면)
    ├── home           (홈/상담 목록 화면)
    ├── settings       (설정 화면)
    └── summary        (최근 상태 요약 화면)

```
