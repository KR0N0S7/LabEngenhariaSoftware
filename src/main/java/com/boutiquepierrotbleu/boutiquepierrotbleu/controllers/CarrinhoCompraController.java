package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CarrinhoCompraService;

@Controller
public class CarrinhoCompraController {

    @Autowired
    private CarrinhoCompraService carrinhoCompraService;

    @RequestMapping(value = "/criarCarrinho", method = RequestMethod.GET)
    public ModelAndView criarCarrinho() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/detalharCarrinho", method = RequestMethod.GET)
    public ModelAndView detalharCarrinho(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/adicionarItemNoCarrinho", method = RequestMethod.GET)
    public ModelAndView adicionarItemNoCarrinho(Long id, Long produtoId, Integer quantity) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/removerItemDoCarrinho", method = RequestMethod.GET)
    public ModelAndView removerItemDoCarrinho(Long id, Long itemId) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/deletarCarrinho", method = RequestMethod.GET)
    public ModelAndView deletarCarrinho(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }
}
