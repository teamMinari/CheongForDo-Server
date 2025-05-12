package Minari.cheongForDo.domain.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatResponse {
    public List<Choice> choices;

    @Getter
    @Setter
    public static class Choice {
        public Message message;

        @Getter
        @Setter
        public static class Message {
            public String role;
            public String content;
        }
    }
}
