package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.CarrinhoCompra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemProduto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CarrinhoCompraService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ClienteService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ItemProdutoService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ProdutoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("carrinho")
public class CarrinhoCompraController {

    @Autowired
    private CarrinhoCompraService carrinhoCompraService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ItemProdutoService itemProdutoService;

    private static final Logger logger = LoggerFactory.getLogger(CarrinhoCompraController.class);

    public Cliente obterCliente(HttpSession session) throws Exception {
        Long clienteId = (Long) session.getAttribute("clienteId");
        return clienteService.obterCliente(clienteId);
    }
    
    public CarrinhoCompra criarCarrinho(Long produtoId) {
        CarrinhoCompra carrinho = new CarrinhoCompra();
        return carrinhoCompraService.salvarCarrinhoCompra(carrinho);
    }

    @RequestMapping(value = "/detalhar", method = RequestMethod.GET)
    public ModelAndView detalharCarrinho(HttpSession session, Long id) throws Exception {
        ModelAndView mv = new ModelAndView("carrinho/index");
        Long idCliente = (Long) session.getAttribute("id");
        CarrinhoCompra carrinho = carrinhoCompraService.getOrCreateCart(clienteService.obterCliente(idCliente));
        List<ItemProduto> listaProdutos = carrinho.getItemProduto();
        if(!carrinho.getItemProduto().equals(null)) {
            mv.addObject("total", carrinho.getValorTotal());
            mv.addObject("lista", listaProdutos);
        } else {
            mv.addObject("message", "Carrinho vazio!");
        }
        return mv;
    }

    @RequestMapping(value = "/adicionar", method = RequestMethod.POST)
    public ModelAndView adicionarItemNoCarrinho(HttpSession session, Long produtoId) throws Exception {
        Long clienteId = (Long) session.getAttribute("id");
        Cliente cliente = clienteService.obterCliente(clienteId);
        Produto produto = produtoService.obterProduto(produtoId);
        ModelAndView modelAndView;

        CarrinhoCompra carrinho = carrinhoCompraService.getOrCreateCart(cliente);
        
        if(carrinho.getItemProduto().equals(null)) {
            carrinho.setItemProduto(new ArrayList<ItemProduto>());
        }

        
        if(cliente == null) {
            // Handle unauthenticated user
            modelAndView = new ModelAndView("redirect:/cliente/login");
        } else {
            // Create ItemProduto and add to cart.
            if(carrinho.getItemProduto().stream().anyMatch(item -> item.getProduto().getNome().equals(produto.getNome()))) {
                // Sum products that already has in cart
                int index = carrinho.getItemProduto().indexOf(carrinho.getItemProduto().stream().filter(item -> item.getProduto().getNome().equals(produto.getNome())).findFirst().get());
                carrinho.getItemProduto().get(index).setQuantidade(carrinho.getItemProduto().get(index).getQuantidade() + 1);
            } else {
                ItemProduto itemProduto = itemProdutoService.salvaItemProduto(new ItemProduto());
                    itemProduto.setCarrinhoCompra(carrinho);
                    itemProduto.setPreco(produto.getPreco());
                    itemProduto.setProduto(produto);
                    itemProduto.setQuantidade(1);
                carrinho.getItemProduto().add(itemProduto);
                // Handle other necessary properties of itemProduto...
                logger.debug("Produto::::: {}", produto.getNome());
                logger.debug("Lista de ItemProduto::::: {}", carrinho.getItemProduto().toString());
                carrinhoCompraService.addItemProdutoToCart(cliente, itemProduto);
            }

            carrinho.calcularValorTotal();

            // If you want to redirect to the cart view page
            // modelAndView = new ModelAndView("redirect:/carrinho");

            // If you want to show a specific page with some model attributes
            modelAndView = new ModelAndView("redirect:/produto/listarProdutos"); // Specify your page name
            modelAndView.addObject("message", "Produto adicionado com sucesso!");
            modelAndView.addObject("carrinho", carrinho);
            modelAndView.addObject("id", session.getAttribute("id"));
            modelAndView.addObject("numeroProdutos", carrinho.getItemProduto().size());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/remover", method = RequestMethod.POST)
    public String removerItemDoCarrinho(Long carrinhoId, Long itemId) {
        try {
            carrinhoCompraService.removerItemDoCarrinho(carrinhoId, itemId);
            return "redirect:/detalharCarrinho?id=" + carrinhoId;
        } catch (Exception e) {
            // Log error and redirect to an error page or handle accordingly
            return "redirect:/errorPage";
        }
    }


    @RequestMapping(value = "/deletarCarrinho", method = RequestMethod.GET)
    public ModelAndView deletarCarrinho(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }
}
