package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    public CarrinhoCompra criarCarrinho(Long id) {
        CarrinhoCompra carrinho = new CarrinhoCompra();
        return carrinhoCompraService.salvarCarrinhoCompra(carrinho);
    }

    @RequestMapping(value = "/detalhar", method = RequestMethod.GET)
    public ModelAndView detalharCarrinho(HttpSession session, @RequestParam(required = false) Long id)
            throws Exception {
        ModelAndView mv;

        logger.debug("Sessionnnnnnnnn !!!!!!::::: {}", session.getAttribute("id"));

        if (session.getAttribute("id") == null) {
            mv = new ModelAndView("redirect:/cliente/login");
        } else {
            mv = new ModelAndView("carrinho/index");
            List<ItemProduto> listaProdutos = new ArrayList<ItemProduto>();
            Long idCliente = (Long) session.getAttribute("id");
            CarrinhoCompra carrinho = carrinhoCompraService.getOrCreateCart(clienteService.obterCliente(idCliente));
            listaProdutos = carrinho.getItemProduto();
            logger.debug("Carrinho ativo aqui !!!!!!::::: {}", carrinho.isAtivo());

            mv.addObject("carrinho", carrinho);
            if (carrinho.getItemProduto() != null) {
                mv.addObject("total", carrinho.getValorTotal());
                mv.addObject("lista", listaProdutos);
            } else {
                // mv.addObject("message", "Carrinho vazio!");
            }
        }
        return mv;

    }

    @RequestMapping(value = "/adicionar", method = RequestMethod.POST)
    public ModelAndView adicionarItemNoCarrinho(HttpSession session,
            @RequestParam(value = "produtoId") Long produtoId,
            @RequestParam(value = "quantidade") Integer quantity) throws Exception {

        Long clienteId = (Long) session.getAttribute("id");

        Cliente cliente = clienteService.obterCliente(clienteId);
        Produto produto = produtoService.obterProduto(produtoId);
        ModelAndView modelAndView;

        CarrinhoCompra carrinho = carrinhoCompraService.getOrCreateCart(cliente);

        if (carrinho.getItemProduto().equals(null)) {
            carrinho.setItemProduto(new ArrayList<ItemProduto>());
        }

        if (cliente == null) {
            // Handle unauthenticated user
            modelAndView = new ModelAndView("redirect:/cliente/login");
        } else {
            modelAndView = new ModelAndView("redirect:/produto/listarProdutos");
            ItemProduto itemProduto;
            // Create ItemProduto and add to cart.
            if (carrinho.getItemProduto().stream()
                    .anyMatch(item -> item.getProduto().getId().equals(produto.getId()))) {
                if (produto.isStockAvailable(1)) {
                    // Sum products that already has in cart
                    int index = carrinho.getItemProduto().indexOf(carrinho.getItemProduto().stream()
                            .filter(item -> item.getProduto().getNome().equals(produto.getNome())).findFirst().get());
                    itemProduto = carrinho.getItemProduto().get(index);
                    itemProduto.setQuantidade(itemProduto.getQuantidade() + quantity);
                    itemProduto.setPreco(produto.getPreco(), itemProduto.getQuantidade());
                    logger.debug("Número de {}::::: {}", itemProduto.getProduto().getNome(),
                            itemProduto.getQuantidade());
                    itemProdutoService.salvaItemProduto(itemProduto);

                    produto.decreaseEstoque(quantity);
                    produtoService.salvarProduto(produto);
                } else {
                    modelAndView.addObject("message", "Estoque insuficiente!");
                }
            } else {
                if (produto.isStockAvailable(quantity)) {
                    itemProduto = itemProdutoService.salvaItemProduto(new ItemProduto());
                    itemProduto.setCarrinhoCompra(carrinho);
                    itemProduto.setPreco(produto.getPreco(), quantity);
                    itemProduto.setProduto(produto);
                    itemProduto.setQuantidade(quantity);
                    carrinho.getItemProduto().add(itemProduto);
                    // Handle other necessary properties of itemProduto...
                    logger.debug("Produto::::: {}", produto.getNome());
                    logger.debug("Lista de ItemProduto::::: {}", carrinho.getItemProduto().toString());
                    // carrinho.addItemProduto(itemProduto); //a entidade é a responsável por
                    // verificar disponibilidade

                    logger.debug("Valor total antes::::: {}", carrinho.getValorTotal());
                    Double valorTotal = carrinho.calcularValorTotal(carrinho);
                    carrinho.setValorTotal(valorTotal);
                    carrinhoCompraService.salvarCarrinhoCompra(carrinho);
                    logger.debug("Valor total depois::::: {}", valorTotal);
                    // If you want to redirect to the cart view page
                    // modelAndView = new ModelAndView("redirect:/carrinho");

                    modelAndView.addObject("message", "Produto adicionado com sucesso!");
                    modelAndView.addObject("carrinho", carrinho);
                    modelAndView.addObject("id", session.getAttribute("id"));

                    produto.decreaseEstoque(1);
                    produtoService.salvarProduto(produto);
                } else {
                    modelAndView.addObject("message", "Produto não disponível no estoque!");
                }
            }

            Integer numeroProdutos = carrinho.getItemProduto().stream()
                    .mapToInt(ItemProduto::getQuantidade)
                    .sum();
            modelAndView.addObject("numeroProdutos", numeroProdutos);

            logger.debug("Número de produtos::::: {}", numeroProdutos);
        }
        return modelAndView;
    }

    @RequestMapping("/remover")
    public ModelAndView removerItemDoCarrinho(@RequestParam("id") Long itemId, RedirectAttributes redirectAttributes)
            throws Exception {
        ModelAndView mv = new ModelAndView("redirect:/carrinho/detalhar");
        ;
        Long carrinhoId = itemProdutoService.obterItemProduto(itemId).getCarrinhoCompra().getId();
        CarrinhoCompra carrinho = carrinhoCompraService.obterCarrinhoCompra(carrinhoId);
        ItemProduto item = itemProdutoService.obterItemProduto(itemId); // This method should throw an
                                                                        // EntityNotFoundException if not found

        logger.debug("Id do produto::::: {}", itemId);
        logger.debug("Id do carrinho::::: {}", carrinhoId);

        if (carrinho.getItemProduto().size() >= 1) {

            Produto produto = item.getProduto();
            produto.increaseEstoque(item.getQuantidade()); // This method should update the estoque and save the entity
            produtoService.salvarProduto(produto);

            carrinho.removeItemProduto(item);
            itemProdutoService.deletarItemProduto(itemId);
            carrinho.setValorTotal(carrinho.calcularValorTotal(carrinho));

            try {
                carrinhoCompraService.salvarCarrinhoCompra(carrinho);
                redirectAttributes.addFlashAttribute("mensagem", "Produto removido com sucesso!");
                logger.debug("Remoção de produto::::::::::::::::::::::::::::::");
            } catch (Exception e) {
                // Log error and redirect to an error page or handle accordingly
                redirectAttributes.addFlashAttribute("mensagem", "Erro ao remover produto!");
                logger.error("Error removing item from cart", e);
            }
        } /*
           * else {
           * 
           * Produto produto = item.getProduto();
           * produto.increaseEstoque(item.getQuantidade()); // This method should update
           * the estoque and save the entity
           * produtoService.salvarProduto(produto);
           * 
           * itemProdutoService.deletarItemProduto(itemId);
           * 
           * carrinhoCompraService.excluirCarrinhoCompra(carrinhoId);
           * redirectAttributes.addFlashAttribute("mensagem", "Carrinho vazio!");
           * logger.debug("Exclusão de carrinho::::::::::::::::::::::::::::::");
           * }
           */

        return mv;
    }

    @RequestMapping("/deletar")
    public ModelAndView deletarCarrinho(Long id) {
        ModelAndView mv = new ModelAndView("redirect:/cliente/cadastro");
        // Your implementation here
        return mv;
    }

    @RequestMapping("/aumentar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> aumentarQuantidade(@RequestParam Long itemProdutoId,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            ItemProduto itemProduto = itemProdutoService.obterItemProduto(itemProdutoId);
            Produto produto = itemProduto.getProduto();
            CarrinhoCompra carrinho = itemProduto.getCarrinhoCompra();

            if (produto.getEstoque() == 0) {
                response.put("success", false);
                response.put("message", "Estoque insuficiente!");
            } else {
                itemProduto.setQuantidade(itemProduto.getQuantidade() + 1);
                produto.decreaseEstoque(1);
                itemProdutoService.salvaItemProduto(itemProduto);
                produtoService.salvarProduto(produto);
                carrinho.setValorTotal(carrinho.calcularValorTotal(carrinho));
                carrinhoCompraService.salvarCarrinhoCompra(carrinho);

                response.put("success", true);
                response.put("message", "Quantidade aumentada com sucesso!");
                response.put("total", carrinho.getValorTotal());
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao aumentar a quantidade");
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping("/diminuir")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> diminuirQuantidade(@RequestParam Long itemProdutoId,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            ItemProduto itemProduto = itemProdutoService.obterItemProduto(itemProdutoId);
            Produto produto = itemProduto.getProduto();
            CarrinhoCompra carrinho = itemProduto.getCarrinhoCompra();

            if (itemProduto.getQuantidade() > 1) {
                itemProduto.setQuantidade(itemProduto.getQuantidade() - 1);
                produto.increaseEstoque(1);
                itemProdutoService.salvaItemProduto(itemProduto);
                produtoService.salvarProduto(produto);
                carrinho.setValorTotal(carrinho.calcularValorTotal(carrinho));
                carrinhoCompraService.salvarCarrinhoCompra(carrinho);

                response.put("success", true);
                response.put("message", "Quantidade diminuída com sucesso!");
                response.put("total", carrinho.getValorTotal());
            } else if (itemProduto.getQuantidade() == 1) {
                Long carrinhoId = itemProduto.getCarrinhoCompra().getId();
                carrinho = carrinhoCompraService.obterCarrinhoCompra(carrinhoId);
                itemProdutoService.deletarItemProduto(itemProdutoId);
                carrinho.setValorTotal(carrinho.calcularValorTotal(carrinho));
                carrinhoCompraService.salvarCarrinhoCompra(carrinho);
                response.put("success", true);
                response.put("message", "Produto removido do carrinho!");
                response.put("total", carrinho.getValorTotal());
            } else {
                response.put("success", false);
                response.put("message", "Quantidade inválida!");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao diminuir a quantidade");
        }
        return ResponseEntity.ok(response);
    }

}
