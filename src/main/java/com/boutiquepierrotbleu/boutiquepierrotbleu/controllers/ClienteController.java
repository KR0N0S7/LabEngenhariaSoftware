package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boutiquepierrotbleu.boutiquepierrotbleu.dto.ClienteSearchCriteria;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Endereco;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.criteriaFilter.ClienteRepositoryImpl;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ClienteService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.EnderecoService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.wrapper.ClienteSearchWrapper;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EnderecoService enderecoService;

    private static final Logger logger = LoggerFactory.getLogger(ClienteRepositoryImpl.class);

    @RequestMapping("login")
    public ModelAndView telaLoginCliente(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        logger.debug("Session: {}", session.getAttribute("id") + ", " + session.getAttribute("nome"));
        Object idAttribute = session.getAttribute("id");
        if (idAttribute == null) {
            mv = new ModelAndView("usr/login");
        } else {
            if (idAttribute != null && "admin".equals(idAttribute.toString())) {
                mv = new ModelAndView("redirect:/admin/editar");
                mv.addObject("id", session.getAttribute("id"));
            } else {
                mv = new ModelAndView("redirect:/cliente/cadastro");
                mv.addObject("id", session.getAttribute("id"));
            }
        }
        
        return mv;
    }

    @RequestMapping("login/autenticar")
    public ModelAndView autenticarCliente(@RequestParam("email") String email, @RequestParam("senha") String senha,
            HttpSession session) {
        ModelAndView mv = new ModelAndView();
        logger.debug("Credenciais: {}", email + ", " + senha);
        if(email.contentEquals("admin@admin.co")  && senha.contentEquals("admin")) {
            session.setAttribute("id", "admin");
            mv = new ModelAndView("redirect:/admin/editar");
        } else {
            mv = new ModelAndView("redirect:/");
            try {
                Cliente cliente = clienteService.autenticarCliente(email, senha);
                session.setAttribute("id", cliente.getId());
                session.setAttribute("nome", cliente.getNomeCompleto());
                mv.addObject("id", session.getAttribute("id"));
                mv.addObject("nome", session.getAttribute("nome"));
            } catch (Exception e) {
                mv.addObject("mensagem", e.getMessage());
            }
        }
        return mv;
    }

    @RequestMapping("cadastro")
    public ModelAndView cadastroCliente(@RequestParam("id") Long id, HttpSession session) {
        session.setAttribute("id", id);
        ModelAndView mv = new ModelAndView("usr/index");
        mv.addObject("id", session.getAttribute("id"));
        try {
            String nome = clienteService.obterCliente((Long) session.getAttribute("id")).getNomeCompleto();
            session.setAttribute("nome", nome);
        } catch (Exception e) {
            e.getMessage();
        }
        return mv;
    }

    @RequestMapping("novo")
    public ModelAndView salvarCliente(@RequestParam(required = false) Long id, HttpSession session) {
        session.setAttribute("id", id);
        ModelAndView mv = new ModelAndView("usr/cadastro");
        Cliente cliente = new Cliente();
        List<Endereco> enderecos = new ArrayList<>();
        if(id != null) {
            try {
                cliente = clienteService.obterCliente(id);
                mv.addObject("cliente", cliente);
                mv.addObject("enderecoCount", cliente.getEnderecos().size());
                logger.debug("Received cliente from db: {}", cliente.getEnderecos().size());
                // enderecos = enderecoService.getEnderecosByClienteId(id);
                // mv.addObject("enderecos", enderecos); 
                mv.addObject("id", session.getAttribute("id"));
                return mv;
            } catch (Exception e) {
                mv.addObject("cliente", e.getMessage());
            }
        }
        mv.addObject("cliente", cliente);
        mv.addObject("enderecos", enderecos); 
        return mv;
    }

    @RequestMapping("editar")
    public ModelAndView editarCliente(@RequestParam(required = false) Long id, HttpSession session) {
        session.setAttribute("id", id);
        ModelAndView mv = new ModelAndView("usr/cadastro");
        Cliente cliente = new Cliente();
        List<Endereco> enderecos = new ArrayList<>();
        if(id != null) {
            try {
                cliente = clienteService.obterCliente(id);
                mv.addObject("cliente", cliente);
                mv.addObject("enderecoCount", cliente.getEnderecos().size());
                logger.debug("Received cliente from db: {}", cliente.getEnderecos().size());
                // enderecos = enderecoService.getEnderecosByClienteId(id);
                // mv.addObject("enderecos", enderecos); 
                return mv;
            } catch (Exception e) {
                mv.addObject("cliente", e.getMessage());
            }
        }
        mv.addObject("cliente", cliente);
        mv.addObject("enderecos", enderecos); 
        mv.addObject("id", session.getAttribute("id"));
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST, path = "salvar")
    public ModelAndView clienteSalvo(@ModelAttribute("cliente") Cliente cliente, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, HttpSession session) {
        logger.debug("Received endereco from form: {}", cliente.getEnderecos().size());
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
        Cliente savedCliente = clienteService.salvarCliente(cliente);
        List<Endereco> enderecos = savedCliente.getEnderecos();
        if (enderecos != null) {
            // iterate over enderecos
            // Assuming enderecos is a list of Endereco objects in Cliente
            for (Endereco endereco : enderecos) {
                endereco.setCliente(savedCliente);
                enderecoService.salvarEndereco(endereco);
            }
        } else {
            // handle the case where enderecos is null
            enderecos = new ArrayList<>();
        }

        if (novo) {
            mv.addObject("cliente", new Cliente());
        } else {
            mv.addObject("cliente", cliente);
            mv.addObject("id", session.getAttribute("id"));
        }
        redirectAttributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
        return mv;
    }

    @RequestMapping("listar")
    public ModelAndView listarClientes() {
        ModelAndView mv = new ModelAndView("adm/list");
        try {
            mv.addObject("lista", clienteService.listarClientes());
            //mv.addObject("wrapper", new ClienteSearchWrapper());
            //mv.addObject("nome", "Usuário");
        } catch (Exception e) {
            mv.addObject("mensagem", "Um erro ocorreu ao listar clientes: " + e.getMessage());
        }
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

    @PostMapping("/updateAtivo")
    public ModelAndView updateAtivoStatus(@RequestParam("clienteId") Long clienteId, RedirectAttributes attributes) {
        ModelAndView mv = new ModelAndView("redirect:/cliente/listar");
        clienteService.toggleAtivoStatusById(clienteId);
        attributes.addFlashAttribute("message", "Status atualizado com sucesso!");
        return mv;
    }

    @RequestMapping("/search")
    public ModelAndView searchClients(@ModelAttribute("wrapper") ClienteSearchWrapper wrapper) {
        List<ClienteSearchCriteria> criterias = wrapper.getCriteriaList();
        logger.debug("Received criteria from List Page: {}", criterias);
        ModelAndView mv = new ModelAndView("adm/list");
        List<List<Cliente>> results = new ArrayList<>();

        for (ClienteSearchCriteria criteria : criterias) {
            results.add(clienteService.searchClients(criteria));
        }

        List<Cliente> clientes = results.stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
            
        mv.addObject("lista", clientes);

        logger.debug("Clients filtered by criteria: {}", clientes);
        return mv;
    }
}
