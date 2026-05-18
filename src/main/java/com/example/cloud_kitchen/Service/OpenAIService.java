package com.example.cloud_kitchen.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    public Map<String, String> analyzeComment(String comment) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");

        message.put("content",
                "Analyze this restaurant review and return ONLY a valid JSON in this format:\n" +
                        "{ \"sentiment\": \"POSITIVE|NEGATIVE|NEUTRAL\", \"reason\": \"short explanation\" }\n\n" +
                        "Review: " + comment
        );

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4.1-mini");
        body.put("messages", List.of(message));
        body.put("temperature", 0);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, request, String.class);

        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response.getBody());

            String content = root
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText()
                    .trim();

            JsonNode analysis = mapper.readTree(content);

            Map<String, String> responseMap = new HashMap<>();

            responseMap.put("review", comment);
            responseMap.put("sentiment", analysis.get("sentiment").asText());
            responseMap.put("reason", analysis.get("reason").asText());

            return responseMap;

        } catch (Exception e) {
            throw new RuntimeException("Error parsing OpenAI response");
        }
    }
}