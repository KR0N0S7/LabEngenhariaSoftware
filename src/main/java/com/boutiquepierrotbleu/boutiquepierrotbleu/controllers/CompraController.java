package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.CarrinhoCompra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Compra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Creditcard;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cupom;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Endereco;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemProduto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Pagamento;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CarrinhoCompraService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ClienteService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CompraService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CreditcardService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CupomService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.EnderecoService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ProdutoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("compra")
@SessionAttributes("compra")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CupomService cupomService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private CreditcardService creditcardService;

    @Autowired
    private CarrinhoCompraService carrinhoCompraService;

    @Autowired
    private ClienteService clienteService;

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    @ModelAttribute("compra")
    public Compra initializeCompra(HttpServletRequest request, CarrinhoCompra carrinhoCompra) {
        if (request.getRequestURI().endsWith("/listar")) {
            return null;  // or Optional.empty() based on your setup
        }
        return new Compra(carrinhoCompra);
    }


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

        // ESVAZIAR CARRINHO DE COMPRA!!!!!
        return mv;
    }

    @RequestMapping("iniciar")
    public ModelAndView iniciarCompra(@ModelAttribute("compra") Compra compra, HttpSession session, CarrinhoCompra carrinhoCompra) {
        ModelAndView mv = new ModelAndView("pages/comprar");
        Long userId = (Long) session.getAttribute("id");
        List<Endereco> enderecos = enderecoService.getEnderecosByClienteId(userId);
        logger.debug("Compra:::::::: {}", compra.getValorTotal());
        //mv.addObject("endereco", new Endereco());
        mv.addObject("listaEnderecos", enderecos);

        logger.debug("Enderecos:::::::: {}", enderecos);
        return mv;
    }

    @RequestMapping("pagamento")
    public ModelAndView escolherPagamento(@ModelAttribute("compra") Compra compra, HttpSession session, @RequestParam("selectedEndereco") Endereco endereco) {
        ModelAndView mv = new ModelAndView("pages/pagamento");
        Long userId = (Long) session.getAttribute("id");

        logger.debug("Endereco:::::::: {}", endereco.getRua());
        logger.debug("Compra_2:::::::: {}", compra.getValorTotal());

        compra.setEnderecoEntrega(endereco);
        
        List<Creditcard> lista = creditcardService.getCartaoByClienteId(userId);

        mv.addObject("lista", lista);

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
    public ModelAndView detalharCompra(@ModelAttribute("compra") Compra compra, HttpSession session, @RequestParam("selectedCartao") Creditcard cartao) {
        ModelAndView mv = new ModelAndView("compra/resumo");
        Endereco enderecoEnvio = compra.getEnderecoEntrega();
        mv.addObject("endereco", enderecoEnvio);
        mv.addObject("cartao", cartao);

        logger.debug("Cartao:::::::: {}", cartao.getApelidoCartao());
        List<Creditcard> lista = new ArrayList<Creditcard>();
        lista.add(cartao);
        compra.setCartao(lista);

        mv.addObject("compra", compra);
        return mv;
    }

    @RequestMapping(value = "/finalizar", method = RequestMethod.POST)
    public ModelAndView finalizarCompra(@ModelAttribute("compra") Compra compra, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("compra/finalizada");

        Long clienteId = session.getAttribute("id") != null ? (Long) session.getAttribute("id") : null;
        Cliente cliente = clienteService.obterCliente(clienteId);
        compra.setCarrinhoId(carrinhoCompraService.getOrCreateCart(cliente).getId());

        // forma de pagamento inserido manualmente, o certo seria pegar conforme escolha do usuário
        compra.setFormaPagamento(Pagamento.CRÉDITO);
        
        Compra compraFinalizada = compraService.salvarCompra(compra);
        
        CarrinhoCompra carrinho = carrinhoCompraService.obterCarrinhoCompra(compraFinalizada.getCarrinhoId());
        carrinho.setAtivo(false);
        carrinhoCompraService.salvarCarrinhoCompra(carrinho);
        
        logger.debug("Carrinho:::::::: {}", carrinho.getItemProduto().size());
        return mv;
    }

    @RequestMapping("/listar")
    public ModelAndView listarCompras(HttpSession session) {
        ModelAndView mv = new ModelAndView("usr/compra/list");
        Long clienteId = (Long) session.getAttribute("id");
        mv.addObject("lista", compraService.getComprasByClienteId(clienteId));

        logger.debug("Clienteeee id:::::::: {}", clienteId);
        return mv;
    }

    @RequestMapping("/detalhar/{id}")
    public ModelAndView detalharCompra(@RequestParam("id") Long id, HttpSession session) {
        ModelAndView mv = new ModelAndView("usr/compra/detail");
        // Your implementation here
        return mv;
    }
}
