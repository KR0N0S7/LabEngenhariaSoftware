package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Endereco;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.EnderecoRepository;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.EnderecoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("endereco")
public class EnderecoController {

	@Autowired
	private EnderecoService enderecoService;
	
	private static final Logger logger = LoggerFactory.getLogger(EnderecoRepository.class);

	@RequestMapping(path = "novo")
	public ModelAndView editarEnderecoendereco(@RequestParam(required = false) Long id, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("usr/endereco/novo.html");
		logger.debug("Session id: ", session.getAttribute("id"));
		Endereco endereco;
		if (id == null) {
			endereco = new Endereco();
			mv.addObject("idSession", session.getAttribute("id"));
		} else {
			try {
				endereco = enderecoService.obterEndereco(id);
			} catch (Exception e) {
				endereco = new Endereco();
				mv.addObject("mensagem", e.getMessage());
			}
		}
		mv.addObject("endereco", endereco);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "editar")
	public ModelAndView enderecoSalvo(Endereco endereco, BindingResult bidingResult, RedirectAttributes redirectAttributes) {
		if (bidingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("usr/endereco/novo.html");
			mv.addObject("endereco", endereco);
			return mv;
		}
		ModelAndView mv = new ModelAndView("redirect:/endereco/listar");
		boolean novo = true;
		if (endereco != null) {
			novo = false;
		}
		enderecoService.salvarEndereco(endereco);
		if (novo) {
			mv.addObject("endereco", new Endereco());
		} else {
			mv.addObject("endereco", endereco);
		}
		redirectAttributes.addFlashAttribute("mensagem", "Endereco salvo com sucesso.");
		return mv;
	}
	
	@RequestMapping("/listar")
	public ModelAndView listarenderecoes(HttpSession session) {
		ModelAndView mv = new ModelAndView("usr/endereco/list.html");
		Long idSession = (Long) session.getAttribute("id");
		mv.addObject("lista", enderecoService.getEnderecosByClienteId(idSession));
		mv.addObject("nome", "enderecos");
		return mv;
	}
	
	@RequestMapping("/excluir")
	public ModelAndView excluirendereco(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("redirect:/endereco/listar");
		try {
			enderecoService.excluirEndereco(id);
			redirectAttributes.addFlashAttribute("mensagem", "Endereco excluído com sucesso.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensagem", "Erro ao excluir endereco." + e.getMessage());
		}
		return mv;
	}
}
