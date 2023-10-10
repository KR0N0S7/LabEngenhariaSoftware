package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CarrinhoCompraService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ClienteService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ProdutoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CarrinhoCompraService carrinhoCompraService;
    
    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    @RequestMapping("listarProdutos")
    public ModelAndView listarProdutos(HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("produto/page");
        List<Produto> produtos = produtoService.listarProdutos();
        mv.addObject("produtos", produtos);
        Long idCliente = (Long) session.getAttribute("id");
        mv.addObject("id", idCliente);

        Cliente cliente = clienteService.obterCliente(idCliente);
        Integer numProdutosNoCarrinho = carrinhoCompraService.getOrCreateCart(cliente).getItemProduto().size();
        mv.addObject("numeroProdutos", numProdutosNoCarrinho);
        logger.debug("Session id: {}", session.getId());
        logger.debug("Session id: {}", session.getAttribute("id"));
        logger.debug("Nome do cliente: {}", cliente.getNomeCompleto());
        logger.debug("Numero de produtos: {}", numProdutosNoCarrinho);
        return mv;
    }

    @RequestMapping(value = "/detalharProduto", method = RequestMethod.GET)
    public ModelAndView detalharProduto(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/criarProduto", method = RequestMethod.GET)
    public ModelAndView criarProduto() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/atualizarProduto", method = RequestMethod.GET)
    public ModelAndView atualizarProduto(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/deletarProduto", method = RequestMethod.GET)
    public ModelAndView deletarProduto(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/reservarEstoqueDeProduto", method = RequestMethod.GET)
    public ModelAndView reservarEstoqueDeProduto(Long id, Integer quantity) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/liberarEstoqueDeProduto", method = RequestMethod.GET)
    public ModelAndView liberarEstoqueDeProduto(Long id, Integer quantity) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }
}
