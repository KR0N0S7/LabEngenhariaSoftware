package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Endereco;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.EnderecoRepository;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ClienteService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.EnderecoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("endereco")
public class EnderecoController {

	@Autowired
	private EnderecoService enderecoService;

	@Autowired
	private ClienteService clienteService;

	private static final Logger logger = LoggerFactory.getLogger(EnderecoRepository.class);

	@RequestMapping(path = "novo")
	public ModelAndView editarEndereco(@RequestParam(required = false) Long id, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("usr/endereco/novo.html");
		logger.debug("Session id: {}", session.getAttribute("id"));
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

	@PostMapping("editar")
	public ModelAndView enderecoSalvo(Endereco endereco, BindingResult bidingResult,
			RedirectAttributes redirectAttributes, HttpSession session) throws Exception {
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
		// Here, you retrieve the Cliente using the id from the session and set it on
		// the endereco
		Long clienteId = (Long) session.getAttribute("id");
		if (clienteId != null) {
			Cliente cliente = clienteService.obterCliente(clienteId); // This method should be implemented in your
																		// service
			endereco.setCliente(cliente);
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

		logger.debug("Session id: {}", idSession);

		mv.addObject("lista", enderecoService.getEnderecosByClienteId(idSession));
		mv.addObject("nome", "enderecos");
		return mv;
	}

	@RequestMapping("/excluir")
	public ModelAndView excluirEndereco(@RequestParam Long id, RedirectAttributes redirectAttributes) {
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
