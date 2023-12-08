package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ClienteService;

@Controller
@RequestMapping("admin")
public class AdministradorController {

	@Autowired
	private ClienteService clienteService;
	
	@RequestMapping("editar")
	public ModelAndView editarcliente(@RequestParam(required = false) Long id) {
		ModelAndView mv = new ModelAndView("adm/main/index.html");
		Cliente cliente;
		if (id == null) {
			cliente = new Cliente();
		} else {
			try {
				cliente = clienteService.obterCliente(id);
			} catch (Exception e) {
				cliente = new Cliente();
				mv.addObject("mensagem", e.getMessage());
			}
		}
		mv.addObject("admin", cliente);
		return mv;
	}

	@RequestMapping("main")
	public ModelAndView mainAdmin() {
		ModelAndView mv = new ModelAndView("adm/index.html");
		
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "editar")
	public ModelAndView clienteSalvo(Cliente cliente, BindingResult bidingResult, RedirectAttributes redirectAttributes) {
		if (bidingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("admin/cliente/novo");
			mv.addObject("admin", cliente);
			return mv;
		}
		ModelAndView mv = new ModelAndView("redirect:/admin/listar");
		boolean novo = true;
		if (cliente != null) {
			novo = false;
		}
		clienteService.salvarCliente(cliente);
		if (novo) {
			mv.addObject("admin", new Cliente());
		} else {
			mv.addObject("admin", cliente);
		}
		redirectAttributes.addFlashAttribute("clienteCreatedSuccess", true);
		return mv;
	}
	
	@RequestMapping("/listar")
	public ModelAndView listarclientees() {
		ModelAndView mv = new ModelAndView("adm/list.html");
		mv.addObject("lista", clienteService.listarClientes());
		mv.addObject("nome", "clientes");
		return mv;
	}
	
	@RequestMapping("/excluir")
	public ModelAndView excluircliente(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("redirect:/admin/listar");
		try {
			clienteService.excluirCliente(id);
			redirectAttributes.addFlashAttribute("mensagem", "cliente excluído com sucesso.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensagem", "Erro ao excluir cliente." + e.getMessage());
		}
		return mv;
	}

	@RequestMapping("cliente")
	public ModelAndView novoCliente() {
		ModelAndView mv = new ModelAndView("adm/cliente/novo");
		Cliente cliente = new Cliente();
		mv.addObject("cliente", cliente);
		return mv;
	}


}
