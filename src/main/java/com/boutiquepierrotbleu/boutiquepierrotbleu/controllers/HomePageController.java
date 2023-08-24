package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController {
    @GetMapping("/")
    public ModelAndView homepage() {
<<<<<<< HEAD
          ModelAndView mv = new ModelAndView("pages/cadastro");
=======
          ModelAndView mv = new ModelAndView("pages/home");
>>>>>>> 0d0d7f6 (CRUD completo de Cliente)
        return mv;
    }
}