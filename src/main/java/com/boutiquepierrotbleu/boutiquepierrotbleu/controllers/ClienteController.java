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
@RequestMapping(path = "cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @RequestMapping("novo")
    public ModelAndView salvarCliente(@RequestParam(required = false) Long id) {
        ModelAndView mv = new ModelAndView("pages/cadastro");
        Cliente cliente = new Cliente();
        if(id != null) {
            try {
                mv.addObject("cliente", clienteService.obterCliente(id));
                return mv;
            } catch (Exception e) {
                mv.addObject("cliente", e.getMessage());
            }
        }
        mv.addObject("cliente", cliente);
        return mv;
    }


    @RequestMapping(method = RequestMethod.POST, path = "salvar")
    public ModelAndView clienteSalvo(Cliente cliente, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("cliente/cadastro");
            mv.addObject("cliente", cliente);
            return mv;
        }
        ModelAndView mv = new ModelAndView(/* "redirect:/cliente/novo"*/"pages/cadastrocompleto");
        Boolean novo = true;
        if (cliente != null) {
            novo = false;
        } 
        clienteService.salvarCliente(cliente);
        if (novo) {
            mv.addObject("cliente", new Cliente());
        } else {
            mv.addObject("cliente", cliente);
        }
        redirectAttributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
        return mv;
    }

    @RequestMapping("listar")
    public ModelAndView listarClientes() {
        ModelAndView mv = new ModelAndView("admin/listar");
        mv.addObject("lista", clienteService.listarClientes());
        mv.addObject("nome", "Usuário");
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
