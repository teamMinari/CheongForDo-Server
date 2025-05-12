package Minari.cheongForDo.domain.chatbot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// 사용자 요청용
public class ChatRequest {
    private String question;
}
