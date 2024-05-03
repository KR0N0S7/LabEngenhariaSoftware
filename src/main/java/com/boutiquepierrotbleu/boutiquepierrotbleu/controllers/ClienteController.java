package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.weaver.ast.Not;
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
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Compra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Endereco;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemProduto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.NotasProdutos;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.integrations.JsonUtil;
import com.boutiquepierrotbleu.boutiquepierrotbleu.integrations.RecomendaProduto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.criteriaFilter.ClienteRepositoryImpl;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ClienteService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CompraService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.EnderecoService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.NotasProdutosService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ProdutoService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.wrapper.ClienteSearchWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private NotasProdutosService notasProdutosService;

    @Autowired
    private RecomendaProduto recomendaProduto;

    private static final Logger logger = LoggerFactory.getLogger(ClienteRepositoryImpl.class);

    @RequestMapping("login")
    public ModelAndView telaLoginCliente(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        logger.debug("Session: {}", session.getAttribute("id") + ", " + session.getAttribute("nome"));
        Object idAttribute = session.getAttribute("id");

        if (idAttribute == null) {
            mv = new ModelAndView("usr/login");
        } else {
            logger.debug("Session ID::::::::::::::::::: {}", idAttribute);
            logger.debug("Logica!!!!!!!!!!!!!!::::::::::::::::::: {}", idAttribute.equals("admin"));
            if (idAttribute.equals("admin")) {
                logger.debug("Truuuuuuuuuuuuuuuuuuue!!!!!!!!!!!!:::::::::::::::::::");
                mv = new ModelAndView("redirect:/admin/editar");
                // mv.addObject("id", session.getAttribute("id"));
            } else {
                logger.debug("Caiu aqui!!!!!!!!!!!!:::::::::::::::::::");
                mv = new ModelAndView("redirect:/cliente/cadastro");
                mv.addObject("id", session.getAttribute("id"));
            }
        }

        return mv;
    }

    @RequestMapping("login/autenticar")
    public ModelAndView autenticarCliente(@RequestParam String email, @RequestParam String senha,
            HttpSession session) {
        ModelAndView mv = new ModelAndView();
        logger.debug("Credenciais: {}", email + ", " + senha);
        if (email.contentEquals("admin@admin.co") && senha.contentEquals("admin")) {
            session.setAttribute("id", "admin");
            session.setAttribute("nome", "admin");
            mv = new ModelAndView("redirect:/admin/editar");
            List<Compra> compras = compraService.listarCompras();
            Double lucro = 0.0;
            Double valorVenda = 0.0;
            Double valorCusto = 0.0;
            Integer quantiaProduto = 0;
            List<ItemProduto> itens = null;
            for (Compra compra : compras) {
                itens = compra.getItens();
                for (ItemProduto item : itens) {
                    quantiaProduto = item.getQuantidade();
                    valorVenda += item.getProduto().getPreco() * quantiaProduto;
                    valorCusto += item.getProduto().getCusto() * quantiaProduto;
                }
            }
            lucro = valorVenda - valorCusto;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            String numeroFormatado = decimalFormat.format(lucro);
            mv.addObject("vendas", compras.size());
            mv.addObject("clientes", clienteService.listarClientes().size());
            mv.addObject("produtos", produtoService.listarProdutos().size());

            mv.addObject("lucro", numeroFormatado);
        } else {
            mv = new ModelAndView("redirect:/");
            try {
                Cliente cliente = clienteService.autenticarCliente(email, senha);
                session.setAttribute("id", cliente.getId());
                session.setAttribute("nome", cliente.getNomeCompleto());
                List<NotasProdutos> notasGlobais = notasProdutosService.listarNotasProdutos();
                Long clienteId = cliente.getId();
                List<NotasProdutos> notasClienteLogin = notasProdutosService.listarNotasProdutosPorCliente(clienteId);

                System.out.println("Notas ########: " + notasGlobais.size());
                System.out.println("Notas do usuriooooooo ########: " + notasClienteLogin.size());

                Mono<String> recomendacoes = recomendaProduto.sendNotasProduto(notasGlobais, notasClienteLogin);

                recomendacoes.subscribe(
                        data -> {
                            // Here, 'data' is the result of the Mono once it completes
                            try {
                                List<String> lista = new ArrayList<>();
                                lista = JsonUtil.parseJsonToNomeProdutoLista(data);
                                List<Produto> listaProdutosRecomendados = produtoService
                                        .listarProdutosAPartirDeUmaListaDeNomes(lista);
                                session.setAttribute("recomendacoes", listaProdutosRecomendados); // Set to session
                                System.out.println("Recommendations saved in session.");
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        },
                        error -> {
                            // Handle errors here
                            System.err.println("Error fetching recommendations: " + error.getMessage());
                        });

                List<Produto> listaProdutosRecomendados = session.getAttribute("recomendacoes") != null
                        ? (List<Produto>) session.getAttribute("recomendacoes")
                        : new ArrayList<>();

                // mv.addObject("recomendacoes", listaProdutosRecomendados);
                mv.addObject("id", session.getAttribute("id"));
                mv.addObject("nome", session.getAttribute("nome"));
            } catch (Exception e) {
                mv.addObject("mensagem", e.getMessage());
            }
        }
        return mv;
    }

    private List<Integer> parseRecommendations(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, new TypeReference<List<Integer>>() {
            });
        } catch (IOException e) {
            // Log error or throw an exception
            return Collections.emptyList();
        }
    }

    @RequestMapping("cadastro")
    public ModelAndView cadastroCliente(@RequestParam Long id, HttpSession session) {
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
        // Long usrId = (Long) session.getAttribute("id");
        ModelAndView mv = new ModelAndView("usr/cadastro");
        Cliente cliente = new Cliente();
        List<Endereco> enderecos = new ArrayList<>();
        if (id != null) {
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
        // session.setAttribute("id", id);
        ModelAndView mv = new ModelAndView("usr/cadastro");
        Cliente cliente = new Cliente();
        List<Endereco> enderecos = new ArrayList<>();
        if (id != null) {
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

    @PostMapping("salvar")
    public ModelAndView clienteSalvo(@ModelAttribute Cliente cliente, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, HttpSession session) {
        logger.debug("Received endereco from form: {}", cliente.getEnderecos().size());
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("cliente/cadastro");
            mv.addObject("cliente", cliente);
            return mv;
        }
        ModelAndView mv = new ModelAndView(/* "redirect:/cliente/novo" */"pages/cadastrocompleto");
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

    @RequestMapping("senha")
    public ModelAndView alterarSenha(@RequestParam(required = false) Long id) {
        ModelAndView mv = new ModelAndView("usr/senha/alterar");
        Cliente cliente = new Cliente();
        if (id != null) {
            try {
                cliente = clienteService.obterCliente(id);
                mv.addObject("cliente", cliente);
                return mv;
            } catch (Exception e) {
                mv.addObject("cliente", e.getMessage());
            }
        }
        mv.addObject("cliente", cliente);
        return mv;
    }

    @PostMapping("senha/alterada")
    public ModelAndView senhaAlterada(@ModelAttribute Cliente cliente, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, HttpSession session) {

        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("cliente/senha");
            mv.addObject("cliente", cliente);
            return mv;
        }
        ModelAndView mv = new ModelAndView("redirect:/cliente/senha");
        Boolean novo = true;
        if (cliente != null) {
            novo = false;
        }
        Cliente savedCliente = clienteService.salvarCliente(cliente);

        if (novo) {
            mv.addObject("cliente", new Cliente());
        } else {
            mv.addObject("cliente", savedCliente);
            mv.addObject("id", session.getAttribute("id"));
        }
        redirectAttributes.addFlashAttribute("passwordUpdateSuccess", true);
        return mv;
    }

    @RequestMapping("listar")
    public ModelAndView listarClientes() {
        ModelAndView mv = new ModelAndView("adm/list");
        try {
            mv.addObject("lista", clienteService.listarClientes());
            // mv.addObject("wrapper", new ClienteSearchWrapper());
            // mv.addObject("nome", "Usuário");
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
    public ModelAndView updateAtivoStatus(@RequestParam Long clienteId, RedirectAttributes attributes) {
        ModelAndView mv = new ModelAndView("redirect:/cliente/listar");
        clienteService.toggleAtivoStatusById(clienteId);
        attributes.addFlashAttribute("message", "Status atualizado com sucesso!");
        return mv;
    }

    @RequestMapping("/search")
    public ModelAndView searchClients(@ModelAttribute ClienteSearchWrapper wrapper) {
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

    @RequestMapping("/teste")
    public ModelAndView testeClients(HttpSession session) {

        ModelAndView mv = new ModelAndView("pages/teste");

        List<Produto> listaProdutosRecomendados = session.getAttribute("recomendacoes") != null
                ? (List<Produto>) session.getAttribute("recomendacoes")
                : new ArrayList<>();

        for (Produto pro : listaProdutosRecomendados) {
            System.out.println("Produto: " + pro.getNome());
        }

        mv.addObject("lista", listaProdutosRecomendados);
        return mv;
    }
}
