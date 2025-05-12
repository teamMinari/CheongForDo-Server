package Minari.cheongForDo.domain.chatbot.controller;


import Minari.cheongForDo.domain.chatbot.dto.ChatRequest;
import Minari.cheongForDo.domain.chatbot.dto.ChatResponse;
import Minari.cheongForDo.domain.chatbot.service.ChatbotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@Tag(name = "CHATBOT", description = "chatbot API")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatbotService.askQuestion(request);
    }
}
