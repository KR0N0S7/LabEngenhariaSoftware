package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CupomService;

@Controller
public class CupomController {

    @Autowired
    private CupomService cupomService;

    @RequestMapping(value = "/criarCupom", method = RequestMethod.GET)
    public ModelAndView criarCupom() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/detalharCupom", method = RequestMethod.GET)
    public ModelAndView detalharCupom(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/aplicarCupomAUmaCompra", method = RequestMethod.GET)
    public ModelAndView aplicarCupomAUmaCompra(Long id, Long compraId) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/listarCupons", method = RequestMethod.GET)
    public ModelAndView listarCupons() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/deletarCupom", method = RequestMethod.GET)
    public ModelAndView deletarCupom(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }
}
