package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.boutiquepierrotbleu.boutiquepierrotbleu.integrations.ChatbotClient;

@Controller
public class HomePageController {
    private final ChatbotClient chatbotClient;

    // Constructor for dependency injection
    public HomePageController(ChatbotClient chatbotClient) {
        this.chatbotClient = chatbotClient;
    }

    @GetMapping("/")
    public ModelAndView homepage() {
        ModelAndView mv = new ModelAndView("pages/home");
        // We'll integrate the chatbot here soon!
        return mv;
    }

    @PostMapping("/send-message")
    public ModelAndView sendMessage(@RequestParam("message") String userMessage) {
        ModelAndView mv = new ModelAndView("pages/home");

        chatbotClient.getChatbotResponse(userMessage)
                .subscribe(chatbotResponse -> {
                    // Add the chatbot's response to the Model
                    mv.addObject("chatbotResponse", chatbotResponse);
                });

        return mv;
    }
}