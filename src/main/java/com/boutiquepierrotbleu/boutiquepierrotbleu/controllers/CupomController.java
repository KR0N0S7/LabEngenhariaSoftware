package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cupom;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CupomService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("cupom")
public class CupomController {

    @Autowired
    private CupomService cupomService;

    private static final Logger logger = LoggerFactory.getLogger(CupomController.class);

    @RequestMapping(value = "/criar", method = RequestMethod.GET)
    public ModelAndView criarCupom() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/detalhar", method = RequestMethod.GET)
    public ModelAndView detalharCupom(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/aplicar", method = RequestMethod.GET)
    public ModelAndView aplicarCupomAUmaCompra(Long id, Long compraId) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping("/listar")
    public ModelAndView listarCupons(HttpSession session) {
        ModelAndView mv = new ModelAndView("usr/cupom/list");
        Long clienteId = (Long) session.getAttribute("id");
        logger.debug("Cliente ID::::::::: {}", clienteId);
        List<Cupom> lista = cupomService.listarCuponsByClienteId(clienteId);
        mv.addObject("lista", lista);
        logger.debug("Número de Cupons::::::::: {}", lista.size());
        return mv;
    }

    @RequestMapping(value = "/deletar", method = RequestMethod.GET)
    public ModelAndView deletarCupom(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping("/validate")
    public ResponseEntity<String> validateCupom(@RequestBody String cupomCode) {
        // Logic to validate the coupon code
        boolean isValid = cupomService.validateCupom(cupomCode);
        
        // Return the appropriate response
        if(isValid) {
            return ResponseEntity.ok("Coupon is valid");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid coupon");
        }
    }
}
