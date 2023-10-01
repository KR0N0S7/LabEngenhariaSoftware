package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ProdutoService;

@Controller
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @RequestMapping(value = "/listarProdutos", method = RequestMethod.GET)
    public ModelAndView listarProdutos() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/detalharProduto", method = RequestMethod.GET)
    public ModelAndView detalharProduto(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/criarProduto", method = RequestMethod.GET)
    public ModelAndView criarProduto() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/atualizarProduto", method = RequestMethod.GET)
    public ModelAndView atualizarProduto(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/deletarProduto", method = RequestMethod.GET)
    public ModelAndView deletarProduto(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/reservarEstoqueDeProduto", method = RequestMethod.GET)
    public ModelAndView reservarEstoqueDeProduto(Long id, Integer quantity) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/liberarEstoqueDeProduto", method = RequestMethod.GET)
    public ModelAndView liberarEstoqueDeProduto(Long id, Integer quantity) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }
}
