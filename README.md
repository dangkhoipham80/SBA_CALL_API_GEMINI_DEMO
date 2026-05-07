# SBA_CALL_API_GEMINI_DEMO

Spring Boot demo gọi **Google Gemini Flash 2.5 API**, hỗ trợ chat nhiều lượt với lịch sử lưu vào H2 in-memory database.

---

## Tech Stack

| Thành phần                  | Version           |
| --------------------------- | ----------------- |
| Java                        | 21                |
| Spring Boot                 | 3.5.0             |
| Spring Data JPA             | (managed by Boot) |
| H2 Database                 | (managed by Boot) |
| SpringDoc OpenAPI (Swagger) | 2.8.6             |
| Lombok                      | (managed by Boot) |
| JaCoCo                      | 0.8.12            |
| Spring Boot DevTools        | (managed by Boot) |
| Docker                      | 20+               |

---

## Cấu trúc project

```
src/
├── main/
│   ├── java/com/demo/sba_call_api_gemini_demo/
│   │   ├── config/
│   │   │   ├── GeminiConfig.java          # Bean RestClient gọi Gemini
│   │   │   ├── GeminiProperties.java      # @ConfigurationProperties prefix=gemini
│   │   │   └── SwaggerConfig.java         # OpenAPI metadata
│   │   ├── controller/
│   │   │   ├── GeminiDemoController.java  # /api/v1/gemini/*
│   │   │   └── ChatController.java        # /api/v1/chat/*
│   │   ├── demo/
│   │   │   └── GeminiDemoRunner.java      # CommandLineRunner gọi Gemini lúc startup
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   │   ├── ChatCreateRequest.java
│   │   │   │   ├── ChatMessageRequest.java
│   │   │   │   ├── GeminiApiRequest.java  # Body gửi lên Gemini REST API
│   │   │   │   └── GeminiGenerateRequest.java
│   │   │   └── response/
│   │   │       ├── ApiResponse.java       # Wrapper response chung
│   │   │       ├── ChatMessageResponse.java
│   │   │       ├── ChatSessionResponse.java
│   │   │       ├── ChatTurnResponse.java
│   │   │       ├── GeminiApiResponse.java # Map JSON từ Gemini
│   │   │       └── GeminiGenerateResponse.java
│   │   ├── entity/
│   │   │   ├── ChatSession.java           # @Entity bảng chat_sessions
│   │   │   └── ChatMessage.java           # @Entity bảng chat_messages
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java # @RestControllerAdvice
│   │   ├── repository/
│   │   │   ├── ChatMessageRepository.java  # JpaRepository
│   │   │   ├── ChatSessionRepository.java  # JpaRepository
│   │   │   ├── GeminiRepository.java       # Interface gọi Gemini API
│   │   │   └── impl/
│   │   │       └── GeminiRepositoryImpl.java
│   │   ├── service/
│   │   │   ├── ChatService.java
│   │   │   ├── GeminiService.java
│   │   │   └── impl/
│   │   │       ├── ChatServiceImpl.java
│   │   │       └── GeminiServiceImpl.java
│   │   └── SbaCallApiGeminiDemoApplication.java
│   └── resources/
│       └── application.yml
└── test/
    └── java/com/demo/sba_call_api_gemini_demo/
        ├── BaseUnitTest.java               # @ExtendWith(MockitoExtension.class)
        ├── controller/
        │   ├── ChatControllerTest.java     # @WebMvcTest + MockMvc
        │   └── GeminiDemoControllerTest.java
        ├── dto/response/
        │   └── GeminiApiResponseTest.java
        └── service/impl/
            ├── ChatServiceImplTest.java    # Mockito unit test
            └── GeminiServiceImplTest.java
```

---

## Cài đặt & chạy

### 1. Yêu cầu

- Java 21+
- Maven 3.9+
- Gemini API Key

---

## Cài đặt & chạy

### 1. Yêu cầu

- Java 21+
- Maven 3.9+
- Gemini API Key

---

## Cài đặt & chạy

### 1. Yêu cầu

- Java 21+
- Maven 3.9+
- Gemini API Key (lấy tại https://aistudio.google.com/apikey)

### 2. Cấu hình API Key

Tạo file `.env` ở root project:

```properties
GEMINI_API_KEY=your_real_api_key_here
```

> File `.env` đã được thêm vào `.gitignore`, không bao giờ commit key thật.

### 3. Chạy app

```bash
# Compile + chạy
mvn spring-boot:run

# Hoặc build JAR rồi chạy
mvn clean package -DskipTests
java -jar target/sba-call-api-gemini-demo-0.0.1-SNAPSHOT.jar
```

App mặc định chạy tại: **http://localhost:8080**

### 5. Chạy với Docker

```bash
# Build image
docker build -t gemini-demo .

# Chạy container
docker run -p 8080:8080 -e GEMINI_API_KEY=your_real_key gemini-demo
```

> Không cần cài Java hay Maven trên máy — mọi thứ đã được đóng gói trong image.

Truyền thêm biến tuỳ chọn:

```bash
docker run -p 8080:8080 \
  -e GEMINI_API_KEY=your_real_key \
  -e GEMINI_MODEL=gemini-2.5-flash \
  -e DEMO_STARTUP_CALL_ENABLED=false \
  gemini-demo
```

### 4. Auto reload (DevTools)

Khi đang chạy `mvn spring-boot:run`, mỗi lần **build lại** (Ctrl+F9 trong IntelliJ hoặc `mvn compile`), app tự restart mà không cần dừng tay.

---

## API Endpoints

### Gemini Generate

| Method | URL                       | Mô tả                                |
| ------ | ------------------------- | ------------------------------------ |
| `GET`  | `/api/v1/gemini/health`   | Health check                         |
| `POST` | `/api/v1/gemini/generate` | Gọi Gemini 1 lần (không lưu lịch sử) |

**POST /api/v1/gemini/generate**

Request:

```json
{
  "prompt": "Giải thích định lý Pythagore ngắn gọn"
}
```

Response:

```json
{
  "code": 1000,
  "result": {
    "model": "gemini-2.5-flash",
    "prompt": "Giải thích định lý Pythagore ngắn gọn",
    "answer": "Trong tam giác vuông, bình phương cạnh huyền..."
  }
}
```

---

### Chat (Multi-turn, lưu lịch sử)

| Method | URL                                   | Mô tả                       |
| ------ | ------------------------------------- | --------------------------- |
| `POST` | `/api/v1/chat/sessions`               | Tạo session chat mới        |
| `GET`  | `/api/v1/chat/sessions`               | Danh sách tất cả sessions   |
| `POST` | `/api/v1/chat/sessions/{id}/messages` | Gửi tin nhắn, nhận phản hồi |
| `GET`  | `/api/v1/chat/sessions/{id}/messages` | Xem toàn bộ lịch sử chat    |

**Ví dụ tạo session:**

```json
POST /api/v1/chat/sessions
{ "title": "Học Toán" }
```

**Ví dụ gửi tin nhắn:**

```json
POST /api/v1/chat/sessions/1/messages
{ "message": "Pythagore là gì?" }
```

Response:

```json
{
  "code": 1000,
  "result": {
    "userMessage": {
      "id": 1,
      "role": "user",
      "content": "Pythagore là gì?",
      "createdAt": "..."
    },
    "botReply": {
      "id": 2,
      "role": "model",
      "content": "Đây là định lý...",
      "createdAt": "..."
    }
  }
}
```

> Gemini nhận **toàn bộ lịch sử** của session mỗi lần gửi → "nhớ" được context cuộc trò chuyện.

---

## Swagger UI

Sau khi chạy app, truy cập:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
<img width="1087" height="583" alt="image" src="https://github.com/user-attachments/assets/808386d9-4e97-4e44-bcd4-8f27892845bc" />

---

## H2 Console

Xem dữ liệu trong database trực tiếp:

- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:chatdb`
- **Username**: `sa`
- **Password**: _(để trống)_

> Database là in-memory, mất toàn bộ dữ liệu khi restart app.

---

## Test & Coverage

### Chạy test

```bash
# Chạy test + tạo JaCoCo report
mvn clean test

# Chạy test + kiểm tra coverage thresholds (fail nếu không đạt)
mvn clean verify
```

### Xem coverage report

Mở file sau trong browser:

```
target/site/jacoco/index.html
```

### Coverage thresholds (JaCoCo)

<img width="882" height="224" alt="image" src="https://github.com/user-attachments/assets/be00f974-180e-4731-b5d9-b36d8356ada2" />
<img width="890" height="178" alt="image" src="https://github.com/user-attachments/assets/c00b4f5c-a6eb-44a6-b29d-cc4f63712010" />

Các class được **loại trừ** khỏi coverage check (boilerplate/infra):

- `SbaCallApiGeminiDemoApplication` — main class
- `config/**` — Spring configuration beans
- `entity/**` — JPA entities
- `demo/**` — CommandLineRunner startup
- `GeminiRepositoryImpl` — gọi HTTP thật ra ngoài

Thresholds áp dụng cho phần còn lại (controller, service, dto, exception):

| Metric                    | Minimum |
| ------------------------- | ------- |
| Line coverage (bundle)    | **70%** |
| Branch coverage (bundle)  | **60%** |
| Line coverage (mỗi class) | **50%** |

### Danh sách test hiện tại (18 tests)

| Class                      | Tests | Loại                    |
| -------------------------- | ----- | ----------------------- |
| `ChatControllerTest`       | 6     | `@WebMvcTest` + MockMvc |
| `ChatServiceImplTest`      | 7     | Mockito unit test       |
| `GeminiDemoControllerTest` | 2     | `@WebMvcTest` + MockMvc |
| `GeminiApiResponseTest`    | 2     | Unit test               |
| `GeminiServiceImplTest`    | 1     | Mockito unit test       |

---

## Biến môi trường

| Biến                        | Bắt buộc | Default                                     | Mô tả                          |
| --------------------------- | -------- | ------------------------------------------- | ------------------------------ |
| `GEMINI_API_KEY`            | ✅       | —                                           | API key từ Google AI Studio    |
| `GEMINI_MODEL`              | ❌       | `gemini-2.5-flash`                          | Model Gemini sử dụng           |
| `GEMINI_BASE_URL`           | ❌       | `https://generativelanguage.googleapis.com` | Base URL Gemini API            |
| `GEMINI_TIMEOUT_SECONDS`    | ❌       | `60`                                        | Timeout HTTP request (giây)    |
| `DEMO_STARTUP_CALL_ENABLED` | ❌       | `true`                                      | Tắt/bật gọi Gemini lúc startup |
