package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CompraService;

@Controller
public class CompraController {

    @Autowired
    private CompraService compraService;

    @RequestMapping(value = "/criarCompra", method = RequestMethod.GET)
    public ModelAndView criarCompra() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/detalharCompra", method = RequestMethod.GET)
    public ModelAndView detalharCompra(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/listarCompras", method = RequestMethod.GET)
    public ModelAndView listarCompras() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/deletarCompras", method = RequestMethod.GET)
    public ModelAndView deletarCompras(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }
}
