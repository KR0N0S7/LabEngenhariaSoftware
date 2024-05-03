package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class LogoutController {
    
    @RequestMapping("logout")
    public ModelAndView logout(HttpSession session) {
        // Invalidate the session, removing all attributes bound to the session.
        session.invalidate();
        
        ModelAndView mv = new ModelAndView("redirect:/");
        mv.addObject("session", session);

        // Redirect to the login page (or another page if desired).
        return mv;
    }
}

