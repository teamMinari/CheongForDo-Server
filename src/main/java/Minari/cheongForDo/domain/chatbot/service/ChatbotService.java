package Minari.cheongForDo.domain.chatbot.service;

import Minari.cheongForDo.domain.chatbot.dto.ChatMessage;
import Minari.cheongForDo.domain.chatbot.dto.ChatRequest;
import Minari.cheongForDo.domain.chatbot.dto.ChatResponse;
import Minari.cheongForDo.domain.chatbot.dto.OpenAiChatRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;

@Service
public class ChatbotService {
    @Value("${spring.ai.openai.api-key}")
    private String API_KEY;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public ChatResponse askQuestion(ChatRequest userRequest) {
        RestTemplate restTemplate = new RestTemplate();

        // 사용자가 보낸 질문을 OpenAI API 요청 형식으로 변환
        ChatMessage systemMsg = new ChatMessage("system",
                """
                당신은 경제 전문가입니다. 다음 규칙을 반드시 따르세요:
            
                1. 항상 부드럽고 공손한 존댓말을 사용해야 합니다. 반말은 절대 사용하지 마세요.
            
                2. 이모티콘은 사용하지 마세요.
            
                3. 이름을 묻는 질문에는 "아직 이름은 정해지지 않았습니다."라고만 답변하세요.
                
                4. 경제에 관한 질문에 대해 특히 전문적인 답변을 하세요.
                
                5. 가능하면 경제 관련 질문에만 대답하세요.
                """
        );
        ChatMessage userMsg = new ChatMessage("user", userRequest.getQuestion());

        OpenAiChatRequest openAiRequest = new OpenAiChatRequest();
        openAiRequest.setModel("gpt-3.5-turbo");
        openAiRequest.setMessages(List.of(systemMsg, userMsg));

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        HttpEntity<OpenAiChatRequest> entity = new HttpEntity<>(openAiRequest, headers);

        // OpenAI API 호출
        ResponseEntity<ChatResponse> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                entity,
                ChatResponse.class
        );

        return response.getBody(); // OpenAI 응답 반환
    }
}