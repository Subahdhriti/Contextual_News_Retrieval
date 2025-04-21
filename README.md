# Contextual News Generator

A backend system that fetches, organizes, and enriches news articles based on user queries using natural language processing and LLMs. It understands user intent (e.g., topic, location, relevance) and returns the most contextually appropriate news articles enriched with AI-generated summaries.

---

##  Technologies & Resources Used

- **Java 17** and **Spring Boot 3.x**
- **MongoDB** – Primary database for storing news articles.
- **Redis** – Used to cache LLM summaries via `@Cacheable` to reduce LLM calls.
- **OpenAI API** – For summary extraction.

---

##  API Endpoints

| Endpoint                | Description                                               |
|-------------------------|-----------------------------------------------------------|
| `/api/v1/news/category` | Get articles by category (e.g. `?category=Technology`)    |
| `/api/v1/news/score`    | Get articles with `relevance_score > threshold`           |
| `/api/v1/news/source`   | Get articles from a specific source (e.g. `?source=NDTV`) |
| `/api/v1/news/search`   | Search articles by query in title/description             |
| `/api/v1/news/nearby`   | Get articles near a location (`lat`, `lon`, `radiusInKm`) |

---

##  LLM Integration

- Uses **OpenAI gpt-4o-mini** model via `openai-gpt3-java` library.
- LLM handles generating article summaries.
- Summaries are cached with Redis using `@Cacheable` and `unless` to prevent caching failures.
- The cache keys are a hash generated from the description. If the description
  changes a new summary will be generated and cached.

---

##  Limitations

- OpenAI API is **not free**. This may hit:
    - Rate limits (`HTTP 429`)
    - Quota exhaustion
- Summaries are **only generated once** and cached.
- Parallel LLM calls are rate-limited with backoff + retries.

---

## Exception handling

- Used `GlobalExceptionHandler` and `@RestControllerAdviceto`, ` @ExceptionHandler` handle exception in this
  application.


## CURLs

- /api/v1/news/category
```bash
curl --location 'http://localhost:8080/api/v1/news/category?category=startup'
```
- /api/v1/news/score
```bash
curl --location 'http://localhost:8080/api/v1/news/score?threshold=0.90'
```
- /api/v1/news/source
```bash
curl --location 'http://localhost:8080/api/v1/news/source?source=NDTV'
```
- /api/v1/news/nearby
```bash
curl --location 'http://localhost:8080/api/v1/news/nearby?lat=19.558428&lon=75.569521&radiusInKm=100'
```
- /api/v1/news/search
```bash
curl --location 'http://localhost:8080/api/v1/news/search?query=India'
```
