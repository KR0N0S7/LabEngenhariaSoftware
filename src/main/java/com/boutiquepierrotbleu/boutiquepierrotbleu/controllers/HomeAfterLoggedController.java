package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("home")
public class HomeAfterLoggedController {

    @GetMapping("/")
    public ModelAndView homepage(HttpSession session) {
        ModelAndView mv = new ModelAndView("pages/home");
        if(session.getAttribute("recomendacoes") != null){
            mv.addObject("recomendacoes", session.getAttribute("recomendacoes"));
        }
        return mv;
    }

}