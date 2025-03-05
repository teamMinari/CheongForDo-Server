package Minari.cheongForDo.domain.termary.service;

import Minari.cheongForDo.domain.term.entity.Term;
import Minari.cheongForDo.domain.term.repository.TermRepository;
import Minari.cheongForDo.domain.termary.dto.TermaryRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TermaryService {

    private final RestTemplate restTemplate;
    private final TermRepository termRepository;

    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;

    private final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String summarizeTerm(TermaryRequestDTO termaryRequestDTO) {
        String termNm = termaryRequestDTO.getTermNm();
        Term term = termRepository.findByTermNm(termNm);
        if (term == null) {
            throw new IllegalArgumentException("해당 용어를 찾을 수 없습니다: " + termNm);
        }

        // 이미 요약된 내용이 있으면 그대로 반환
        if (term.getSummary() != null && !term.getSummary().isEmpty()) {
            return term.getSummary();
        }

        // 요약이 없으면 OpenAI API 호출
        String termExplain = term.getTermExplain();
        String prompt = generatePrompt(termExplain);
        String summary = callOpenAiApi(prompt);

        // DB에 저장
        term.setSummary(summary);
        termRepository.save(term);

        return summary;
    }

    private String generatePrompt(String termExplain) {
        return "다음 경제 용어에 대해 설명해 주세요. 설명은 마크다운 형식으로 작성해 주시고, 각 항목은 제목과 내용으로 나누어 주세요.\n\n" +
                "용어: " + termExplain + "\n\n" +
                "설명할 때는 다음과 같은 포맷을 따라주세요:\n" +
                "- **정의**: 용어의 기본 정의\n" +
                "- **중요성**: 용어의 중요성\n" +
                "- **실생활 예시**: 실생활에서의 예시\n" +
                "- **요약**: 용어의 간단한 요약\n\n" +
                "각 항목을 명확히 나누어 주시고, 설명은 청소년이 이해하기 쉬운 언어로 작성해 주세요.";
    }

    private String callOpenAiApi(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openaiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);

        request.put("messages", messages);
//        request.put("max_tokens", 300); // 응답 길이 제한

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                OPENAI_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        // JSON 응답에서 'content'만 추출
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            JsonNode choicesNode = rootNode.path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode messageNode = choicesNode.get(0).path("message").path("content");
                return messageNode.asText().trim();
            } else {
                throw new RuntimeException("API 응답에서 'choices' 노드를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("응답 처리 중 오류가 발생했습니다.", e);
        }
    }
}
