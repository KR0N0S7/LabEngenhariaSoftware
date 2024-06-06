package com.boutiquepierrotbleu.boutiquepierrotbleu.integrations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.reactive.function.client.WebClient;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ChatMessage;

import reactor.core.publisher.Mono;

@Component
@CrossOrigin(origins = "http://localhost:8080")
public class ChatbotClient {
    private static final String CHATBOT_API_URL = "http://127.0.0.1:5001/chat";

    private final WebClient webClient;

    public ChatbotClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(CHATBOT_API_URL).build();
    }

    public Mono<String> getChatbotResponse(String userMessage) {
        ChatMessage requestMessage = new ChatMessage(userMessage);

        return webClient.post()
                .uri(CHATBOT_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestMessage), ChatMessage.class)
                .retrieve()
                .bodyToMono(ChatMessage.class)
                .map(ChatMessage::getResponse);
    }
}
