package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.integrations.ChatbotClient;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomePageController {
    private final ChatbotClient chatbotClient;

    // Constructor for dependency injection
    public HomePageController(ChatbotClient chatbotClient) {
        this.chatbotClient = chatbotClient;
    }

    @GetMapping("/")
    public ModelAndView homepage(HttpSession session) {
        ModelAndView mv = new ModelAndView("pages/home");
        if(session.getAttribute("recomendacoes") != null){
            mv.addObject("recomendacoes", session.getAttribute("recomendacoes"));
        }
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