package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CreditcardService;

@Controller
public class CreditCardController {

    @Autowired
    private CreditcardService creditcardService;

    @RequestMapping(value = "/criarCartao", method = RequestMethod.GET)
    public ModelAndView criarCartao() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/detalharCartao", method = RequestMethod.GET)
    public ModelAndView detalharCartao(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/listarCartaos", method = RequestMethod.GET)
    public ModelAndView listarCartaos() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/deletarCartao", method = RequestMethod.GET)
    public ModelAndView deletarCartao(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }
}
