package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Creditcard;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CreditcardService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("cartao") // Defina o mapeamento raiz para todas as URLs deste controlador
public class CreditCardController {

    @Autowired
    private CreditcardService creditcardService;

    @GetMapping("/novo") // Mapeia "/cartao/novo"
    public ModelAndView editarCreditcard(@RequestParam(required = false) Long id, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("usr/cartao/novo"); // Define o nome da visualização (HTML)
        Creditcard creditcard;
        if (id == null) {
            creditcard = new Creditcard();
            mv.addObject("idSession", session.getAttribute("id"));
        } else {
            try {
                creditcard = creditcardService.obterCreditcard(id);
            } catch (Exception e) {
                creditcard = new Creditcard();
                mv.addObject("mensagem", e.getMessage());
            }
        }
        mv.addObject("creditcard", creditcard);
        return mv;
    }

    @PostMapping(value = "/criarCartao")
    public ModelAndView criarCartao(Creditcard creditcard) {
        ModelAndView mv = new ModelAndView("usr/cartao/novo"); // Define o nome da visualização (HTML)
        creditcardService.salvarCreditcard(creditcard);
        return mv;
    }

    @RequestMapping(value = "/detalharCartao", method = RequestMethod.GET)
    public ModelAndView detalharCartao(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view"); // Define o nome da visualização (HTML)
        // Implemente a lógica para detalhar um cartão aqui
        return mv;
    }

    @RequestMapping(value = "/listarCartaos", method = RequestMethod.GET)
    public ModelAndView listarCartaos() {
        ModelAndView mv = new ModelAndView("path/to/your/view"); // Define o nome da visualização (HTML)
       
        return mv;
    }

    @RequestMapping(value = "/deletarCartao", method = RequestMethod.GET)
    public ModelAndView deletarCartao(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view"); // Define o nome da visualização (HTML)
        // Implemente a lógica para deletar um cartão aqui
        return mv;
    }
}
