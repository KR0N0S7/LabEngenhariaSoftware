package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Creditcard;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ClienteService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CreditcardService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("cartao")
public class CreditCardController {

	@Autowired
	private CreditcardService creditcardService;

	@Autowired
	private ClienteService clienteService;

	private static final Logger logger = LoggerFactory.getLogger(CreditCardController.class);

	@RequestMapping(path = "novo")
	public ModelAndView editarCartao(@RequestParam(required = false) Long id, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("usr/cartao/novo.html");
		logger.debug("Session id: {}", session.getAttribute("id"));
		Creditcard cartao;
		if (id == null) {
			cartao = new Creditcard();
			mv.addObject("idSession", session.getAttribute("id"));
		} else {
			try {
				cartao = creditcardService.obterCreditcard(id);
			} catch (Exception e) {
				cartao = new Creditcard();
				mv.addObject("mensagem", e.getMessage());
			}
		}
		mv.addObject("creditcard", cartao);

		return mv;
	}

	@RequestMapping(method = RequestMethod.POST, path = "editar")
	public ModelAndView cartaoSalvo(Creditcard creditcard,
			BindingResult bidingResult, RedirectAttributes redirectAttributes, HttpSession session) throws Exception {

		if (bidingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("usr/cartao/novo.html");
			mv.addObject("creditcard", creditcard);
			return mv;
		}
		ModelAndView mv = new ModelAndView("redirect:/cartao/listar");
		boolean novo = true;
		if (creditcard != null) {
			novo = false;
		}
		if (!novo) {
			// mv.addObject("creditcard", new Creditcard());
			Cliente cliente = clienteService.obterCliente((Long) session.getAttribute("id"));
			creditcard.setCliente(cliente);
			creditcardService.salvarCreditcard(creditcard);
			redirectAttributes.addFlashAttribute("mensagem", "Cartão salvo com sucesso.");
			logger.debug("Cartão:::::::::::: ", creditcard);
		} else {
			mv.addObject("creditcard", creditcard);
		}

		return mv;
	}

	@RequestMapping("/listar")
	public ModelAndView listarCartaos(HttpSession session) {
		ModelAndView mv = new ModelAndView("usr/cartao/list.html");
		Long idSession = (Long) session.getAttribute("id");
		logger.debug("Session id: ", session.getAttribute("id"));
		mv.addObject("lista", creditcardService.getCartaoByClienteId(idSession));
		mv.addObject("nome", "Cartões");
		return mv;
	}

	@RequestMapping("/excluir")
	public ModelAndView excluirCartao(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("redirect:/usr/cartao/listar");
		try {
			creditcardService.excluirCreditcard(id);
			redirectAttributes.addFlashAttribute("mensagem", "Cartão excluído com sucesso.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensagem", "Erro ao excluir cartão." + e.getMessage());
		}
		return mv;
	}
}
