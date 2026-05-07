# SBA_CALL_API_GEMINI_DEMO

Demo Spring Boot app nho gon de call Gemini Flash 2.5, theo layering:

- controller
- service + service impl
- repository + repository impl
- dto
- config
- exception handler
- test + jacoco

## Chay app tren IntelliJ IDEA

1. Open folder `SBA_CALL_API_GEMINI_DEMO` as Maven project.
2. Sua file `.env` va dien `GEMINI_API_KEY` that.
3. Run class `SbaCallApiGeminiDemoApplication`.

App mac dinh chay port `8099`.

## API demo

- Health: `GET /api/v1/gemini/health`
- Generate: `POST /api/v1/gemini/generate`

Body:

```json
{
  "prompt": "Giai thich dinh ly Pythagore ngan gon"
}
```

## Chay test + jacoco

```bash
mvn clean test
```

Jacoco report sau khi test:

- `target/site/jacoco/index.html`
