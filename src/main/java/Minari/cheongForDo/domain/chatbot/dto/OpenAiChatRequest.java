package Minari.cheongForDo.domain.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenAiChatRequest {
    private String model; // GPT 모델
    private List<ChatMessage> messages; // 메시지 목록
}