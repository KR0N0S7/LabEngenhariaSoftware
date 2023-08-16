package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ClienteService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @RequestMapping(path = "novo")
    public ModelAndView novoCliente() {
        ModelAndView mv = new ModelAndView("");
        mv.addObject("cliente", new Cliente());
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST, path = "salvar")
    public ModelAndView salvarCliente(Cliente cliente, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/cliente/listar");
        if (bindingResult.hasErrors()) {
            mv.addObject("cliente", cliente);
            return mv;
        }
        Cliente clienteSalvo = clienteService.salvarCliente(cliente);
        if (cliente != null) {
            mv.addObject("cliente", new Cliente());
        } else {
            mv.addObject("cliente", clienteSalvo);
        }
        redirectAttributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
        return mv;
    }

    @RequestMapping("/listar")
    public ModelAndView listarClientes() {
        ModelAndView mv = new ModelAndView("cliente/listar");
        mv.addObject("clientes", clienteService.listarClientes());
        mv.addObject("nome", "Usuário");
        return mv;
    }

    @RequestMapping("/editar")
    public ModelAndView editarCliente(@RequestParam Long id) {
        ModelAndView mv = new ModelAndView("cliente/editar");
        Cliente cliente = new Cliente();
        try {
            mv.addObject("cliente", clienteService.obterCliente(id));
        } catch (Exception e) {
            cliente = new Cliente();
            mv.addObject("cliente", e.getMessage());
        }
        mv.addObject("cliente", cliente);
        return mv;
    }

    @RequestMapping("/excluir")
    public ModelAndView excluirCliente(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/cliente/listar");
        try {
            clienteService.excluirCliente(id);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao excluir cliente!");
        }
        return mv;
    }
}
