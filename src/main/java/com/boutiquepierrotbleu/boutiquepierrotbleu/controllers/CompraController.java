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
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Compra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cupom;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Endereco;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemProduto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Pagamento;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CompraService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CupomService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.EnderecoService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ProdutoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("compra")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CupomService cupomService;

    @Autowired
    private EnderecoService enderecoService;

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    /*@RequestMapping(value = "criar", method = RequestMethod.POST)
    public ModelAndView criarCompra(HttpSession session, CarrinhoCompra carrinhoCompra) {
        ModelAndView mv = new ModelAndView("path/to/pagamento");
        Compra compra = new Compra();
        
        // Get the client from the session
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        
        // Validate the items in the cart
        for(ItemProduto item : carrinhoCompra.getItemProduto()) {
            Produto produto = item.getProduto();
            
            // Check stock availability
            if(produto.getEstoque() < item.getQuantidade()) {
                //throw new InsufficientStockException(produto);
            }
            
            // Update stock level
            produto.setEstoque(produto.getEstoque() - item.getQuantidade());
            produtoService.salvarProduto(produto);
        }
        
        // Calculate the total price
        double totalPrice = carrinhoCompra.getItemProduto().stream()
            .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
            .sum();
        
        // Generate a coupon
        Cupom novoCupom = cupomService.generateAndSaveCupom(cliente);

        // Process the payment
        // This might involve calling a payment API and handling the response
        
        // Update the Compra object
        compra.setCliente(cliente);
        compra.setValorTotal(totalPrice);
        compra.setItens(new ArrayList<ItemProduto>(carrinhoCompra.getItemProduto()));
        compra.setCupons(new ArrayList<Cupom>(){
            {
                add(novoCupom);
            }
        });

        // Save the Compra object in the database
        compra = compraService.salvarCompra(compra);
        mv.addObject("compra", compra);
        mv.addObject("message", "Compra realizada com sucesso!");
        return mv;
    }*/

    @RequestMapping("concluir")
    public ModelAndView criarCompra(HttpSession session, CarrinhoCompra carrinhoCompra) {
        ModelAndView mv = new ModelAndView("page/to/compraFinalizada");

        // Get the client from the session
        Cliente cliente = (Cliente) session.getAttribute("cliente");

        // Validate the items in the cart
        for(ItemProduto item : carrinhoCompra.getItemProduto()) {
            Produto produto = item.getProduto();
            
            // Check stock availability
            if(produto.getEstoque() < item.getQuantidade()) {
                //throw new InsufficientStockException(produto);
            }
            
            // Update stock level
            produto.setEstoque(produto.getEstoque() - item.getQuantidade());
            produtoService.salvarProduto(produto);
        }

        // Generate a coupon
        Cupom novoCupom = cupomService.generateAndSaveCupom(cliente);

        Compra compra = new Compra(carrinhoCompra);

        mv.addObject("compra", compra);
        return mv;
    }

    @RequestMapping("iniciar")
    public ModelAndView iniciarCompra(HttpSession session) {
        ModelAndView mv = new ModelAndView("pages/comprar");
        Long userId = (Long) session.getAttribute("id");
        List<Endereco> enderecos = enderecoService.getEnderecosByClienteId(userId);
        //mv.addObject("endereco", new Endereco());
        mv.addObject("listaEnderecos", enderecos);

        logger.debug("Enderecos:::::::: {}", enderecos);
        return mv;
    }

    @RequestMapping("pagamento")
    public ModelAndView escolherPagamento(/*Compra compra, Pagamento pagamento*/) {
        ModelAndView mv = new ModelAndView("pages/pagamento");
        //compra.setFormaPagamento(pagamento);
        //mv.addObject("compra", compra);
        return mv;
    }

    public ModelAndView aplicarCupom(Compra compra, Cupom cupom) {
        ModelAndView mv = new ModelAndView("path/to/finalizar");
        
        Double valorFinal = cupomService.aplicarCupom(cupom, compra.getValorTotal());
        compra.setValorTotal(valorFinal);
        mv.addObject("compra", compra);
        return mv;
    }

    @RequestMapping("detalhar")
    public ModelAndView detalharCompra(/*Long id*/) {
        ModelAndView mv = new ModelAndView("/compra/detail");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/finalizar", method = RequestMethod.POST)
    public ModelAndView finalizarCompra(Compra compra) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        compraService.salvarCompra(compra);
        return mv;
    }

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public ModelAndView listarCompras() {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/deletar", method = RequestMethod.GET)
    public ModelAndView deletarCompras(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }
}
