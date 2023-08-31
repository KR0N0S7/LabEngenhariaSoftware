package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController {
    @GetMapping("/")
    public ModelAndView homepage() {
          ModelAndView mv = new ModelAndView("pages/home");
        return mv;
    }
}