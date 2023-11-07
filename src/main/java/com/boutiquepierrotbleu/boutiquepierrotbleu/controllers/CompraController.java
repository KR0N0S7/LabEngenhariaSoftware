package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemTroca;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Pagamento;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Status;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Troca;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CarrinhoCompraService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ClienteService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CompraService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CreditcardService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CupomService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.EnderecoService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ItemProdutoService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ProdutoService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.TrocaService;

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

    @Autowired
    private TrocaService trocaService;

    @Autowired
    private ItemProdutoService itemProdutoService;

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

    @RequestMapping("/detail")
    public ModelAndView detalharCompra(@RequestParam("id") Long id, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("usr/compra/detail");
        
        Compra compra = compraService.obterCompra(id);
        mv.addObject("compra", compra);
        return mv;
    }

    @RequestMapping("/listarAdmin")
    public ModelAndView listarComprasAdmin() {
        ModelAndView mv = new ModelAndView("adm/compras/listar");
        mv.addObject("lista", compraService.listarCompras());
        return mv;
    }

    public ModelAndView respostaDetalhesCompraAdmin(Long id) throws Exception {
        ModelAndView mv = new ModelAndView("adm/compras/detalhes");
        Compra compra = compraService.obterCompra(id);
        mv.addObject("compra", compra);
        String status = compra.getStatus().toString();
        mv.addObject("status", status);
        List<ItemProduto> itens = compra.getItens();
        mv.addObject("itens", itens);
        Cliente cliente = compra.getCliente();
        mv.addObject("cliente", cliente);
        String nome = cliente.getNomeCompleto();
        mv.addObject("nome", nome);
        Troca troca = new Troca();
        if(status.equals("EM_TROCA") || status.equals("TROCA_AUTORIZADA")) {
            troca = trocaService.obterTrocaByCompraId(id);
            mv.addObject("troca", troca);
        } 
        return mv;
    }

    @RequestMapping("/detalharAdmin")
    public ModelAndView detalharCompraAdmin(@RequestParam("id") Long id, HttpSession session) throws Exception {
        return respostaDetalhesCompraAdmin(id);
    }

    @RequestMapping("/aprovar")
    public ModelAndView aprovarCompra(@RequestParam("id") Long id, HttpSession session) throws Exception {
        Compra compra = compraService.obterCompra(id);
        compra.setStatus(Status.APROVADO);
        compraService.salvarCompra(compra);
        return respostaDetalhesCompraAdmin(id);
    }

    @RequestMapping("/cancelar")
    public ModelAndView cancelarCompra(@RequestParam("id") Long id, HttpSession session) throws Exception {
        Compra compra = compraService.obterCompra(id);
        compra.setStatus(Status.CANCELADO);
        compraService.salvarCompra(compra);
        return respostaDetalhesCompraAdmin(id);
    }

    @RequestMapping("/trocar")
    public ModelAndView trocarCompra(@RequestParam("id") Long id, HttpSession session) throws Exception {
        Compra compra = compraService.obterCompra(id);
        List<ItemProduto> itens = compra.getItens();
        ModelAndView mv = new ModelAndView("usr/compra/troca");
        mv.addObject("itens", itens);
        logger.debug("Compra:::::::: {}", itens.size());
        return mv;
    }

    @RequestMapping("/trocarSelecionados")
    public ModelAndView trocarSelecionados(@RequestParam("selectedItems") List<Long> id, HttpSession session) throws Exception {
        logger.debug("Compra:::::::: {}", id.size());
        Long idCliente = (Long) session.getAttribute("id");
        Compra compra = compraService.obterCompra(idCliente);
        compra.setStatus(Status.EM_TROCA);
        compraService.salvarCompra(compra);
        ModelAndView mv = new ModelAndView("usr/compra/solicitacaoTroca");

        List<Produto> produtos = new ArrayList<Produto>();
        List<ItemTroca> itensTroca = new ArrayList<ItemTroca>();
        for (Long itemProduto : id) {
            ItemProduto item = itemProdutoService.obterItemProduto(itemProduto);
            Produto produto = item.getProduto();
            produtos.add(produto);
            ItemTroca itemTroca = new ItemTroca();
            itemTroca.setProduto(produto);
            itemTroca.setCliente(compra.getCliente());
            itemTroca.setCompra(compra);
            itemTroca.setQuantidade(item.getQuantidade());
            itemTroca.setValor(item.getProduto().getPreco() * item.getQuantidade());
            itensTroca.add(itemTroca);
        }

        Troca troca = trocaService.obterTrocaByCompraId(compra.getId());
        if(troca == null) {
            LocalDate data = LocalDate.now();
    
            troca = new Troca();
            troca.setProdutos(produtos);
            troca.setCliente(compra.getCliente());
            troca.setData(data);
            troca.setStatus(Status.EM_TROCA);
            troca.setCompra(compra);
            trocaService.salvarTroca(troca);
    
            mv.addObject("troca", troca);

        }

        for(ItemTroca itemTroca : itensTroca) {
            itemTroca.setTroca(troca);
            itemTrocaService.salvarItemTroca(itemTroca);
        }
        mv.addObject("itensTroca", itensTroca);

        return mv;
    }
    
    @RequestMapping("/aceitar")
    public ModelAndView aceitarTroca(@RequestParam("id") Long id, HttpSession session) throws Exception {
        Troca troca = trocaService.obterTroca(id);
        Compra compra = troca.getCompra();
        Cliente cliente = compra.getCliente();
        compra.setStatus(Status.TROCA_AUTORIZADA);
        compraService.salvarCompra(compra);
        troca.setStatus(Status.TROCA_AUTORIZADA);
        trocaService.salvarTroca(troca);
        List<ItemProduto> itens = new ArrayList<ItemProduto>();
        for (Produto produto : troca.getProdutos()) {
            List<ItemProduto> item = produto.getItemProduto();
            for(ItemProduto itemProduto : item) {
                itens.add(itemProduto);
            }
        }
        Double valor = troca.calculoValorTroca(itens);
        
        cupomService.gerarCupomTroca(cliente, compra, troca, valor);
        
        return respostaDetalhesCompraAdmin(id);
    }
    
    @RequestMapping("/recusar")
    public ModelAndView recusarTroca(@RequestParam("id") Long id, HttpSession session) throws Exception {
        Compra compra = compraService.obterCompra(id);
        compra.setStatus(Status.REPROVADO);
        compraService.salvarCompra(compra);
        return respostaDetalhesCompraAdmin(id);
    }
}
