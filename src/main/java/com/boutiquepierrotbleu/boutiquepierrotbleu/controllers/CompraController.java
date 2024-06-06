package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ItemTrocaService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.ProdutoService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.TrocaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@ControllerAdvice
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

    @Autowired
    private ItemTrocaService itemTrocaService;

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    public Compra initializeCompra(CarrinhoCompra carrinhoCompra) {
        logger.debug("Chegou Aqui!!!!!!!!!!!!!!!!::::::::");
        return new Compra(carrinhoCompra);
    }

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
    public ModelAndView iniciarCompra(HttpSession session, CarrinhoCompra carrinhoCompra) {
        ModelAndView mv = new ModelAndView("pages/comprar");
        Compra compra = initializeCompra(carrinhoCompra);
        session.setAttribute("compra-obj", compra);
        Long userId = (Long) session.getAttribute("id");
        List<Endereco> enderecos = enderecoService.getEnderecosByClienteId(userId);
        //mv.addObject("endereco", new Endereco());
        mv.addObject("listaEnderecos", enderecos);
        mv.addObject("total", compra.getValorTotal());
        
        logger.debug("Compra:::::::: {}", compra.getValorTotal());
        logger.debug("Enderecos:::::::: {}", enderecos);
        logger.debug("Session:::::::: {}", session.getAttribute("compra-obj"));
        return mv;
    }

    @RequestMapping("cupom")
    public ModelAndView aplicarCupomCompra(HttpSession session, @RequestParam("selectedEndereco") Endereco endereco) {
        ModelAndView mv = new ModelAndView();
        logger.debug("Session cupom:::::::: {}", session.getAttribute("compra-obj"));
        Long userId = (Long) session.getAttribute("id");

        Compra compra = (Compra) session.getAttribute("compra-obj");

        compra.setEnderecoEntrega(endereco);

        logger.debug("Endereco:::::::: {}", endereco.getRua());

        List<Cupom> listaCupom = cupomService.listarCuponsByClienteId(userId);
        List<Cupom> listaCuponsValidos = new ArrayList<>();
        for (Cupom cupom : listaCupom) {
            if (cupom.getUsoLimite() - cupom.getUsoContador() > 0) {
                listaCuponsValidos.add(cupom);
            }
        }

        mv.addObject("listaCupom", listaCuponsValidos);

        if(!listaCupom.isEmpty()) {
            mv.addObject("total", compra.getValorTotal());
            mv.addObject("cupomFlag", true); 
            mv.addObject("compra/pagamento");
        } else {
            mv.addObject("compra/resumo");
            Endereco enderecoEnvio = compra.getEnderecoEntrega();
            logger.debug("Endereço Rua:::::::: {}", enderecoEnvio.getRua());
            mv.addObject("endereco", enderecoEnvio);
        }
        
        logger.debug("Compra:::::::: {}", compra.getValorTotal());
        return mv;
    }

    @PostMapping("/aplicarCupom")
    @ResponseBody
    public ResponseEntity<?> aplicarCupom(@RequestBody String cupomIdStr, HttpSession session) throws Exception {
        logger.debug("Entrou aqui!!!:::::::::::::::::::::::::::::::::::::::::::::::::::");

        // Verificar se o cupomIdStr não é nulo ou vazio
        if (cupomIdStr != null && !cupomIdStr.isEmpty() && !cupomIdStr.equals("\"\"")) {
            cupomIdStr = cupomIdStr.replace("\"", ""); // Remove as aspas extras
            Long cupomId = Long.parseLong(cupomIdStr);

            Compra compra = (Compra) session.getAttribute("compra-obj");
            Cupom cupom = cupomService.obterCupom(cupomId);

            double novoValorTotal = compra.getValorTotal() - cupom.getValor();
            if(novoValorTotal < 0) {
                novoValorTotal = 0;
            }
            compra.setValorFinal(novoValorTotal);

            session.setAttribute("compra", compra);
            session.setAttribute("codigoCupom", cupom.getCodigo());

            logger.debug("Compra_2total:::::::: {}", compra.getValorTotal());
            logger.debug("Compra_2final:::::::: {}", compra.getValorFinal());

            return ResponseEntity.ok(novoValorTotal);
        } else {
            Compra compra = (Compra) session.getAttribute("compra-obj");
            compra.setValorFinal(compra.getValorTotal());
            
            logger.debug("Compra_2retornototal:::::::: {}", compra.getValorTotal());
            logger.debug("Compra_2retornofinal:::::::: {}", compra.getValorFinal());

            return ResponseEntity.ok(compra.getValorTotal());
        }
    }


    @RequestMapping("pagamento")
    public ModelAndView escolherPagamento(HttpSession session) {
        ModelAndView mv = new ModelAndView("pages/pagamento");
        Long userId = (Long) session.getAttribute("id");
        Compra compra = (Compra) session.getAttribute("compra-obj");
        
        logger.debug("Compra_2total:::::::: {}", compra.getValorTotal());
        logger.debug("Compra_2final:::::::: {}", compra.getValorFinal());
        
        mv.addObject("compra", compra.getId());
        mv.addObject("total", compra.getValorFinal());
        
        List<Creditcard> lista = creditcardService.getCartaoByClienteId(userId);

        mv.addObject("lista", lista);

        logger.debug("Session pagamento:::::::: {}", session.getAttribute("compra-obj"));
        logger.debug("Valor Final!!!!!:::::::: {}", compra.getValorFinal());
        // deve ser possível pagar a compra com mais de um cartão de crédito
        return mv;
    }
    
    @RequestMapping("detalhar")
    public ModelAndView detalharCompra(HttpSession session, @RequestParam(required = false, name = "selectedCartao") Creditcard cartao) {
        logger.debug("Session:::::::: {}", session.getAttribute("compra-obj"));
        ModelAndView mv = new ModelAndView("compra/resumo");
        Compra compra = (Compra) session.getAttribute("compra-obj");
        Endereco enderecoEnvio = compra.getEnderecoEntrega();
        if (cartao != null) {
            List<Creditcard> cartoes = new ArrayList<>();
            cartoes.add(cartao);
            compra.setCartao(cartoes);
            mv.addObject("cartoes", cartoes);
        }
        logger.debug("Endereço Rua:::::::: {}", enderecoEnvio.getRua());
        mv.addObject("endereco", enderecoEnvio);
        mv.addObject("total", compra.getValorFinal());

        mv.addObject("compra", compra);
        return mv;
    }

    @PostMapping("/finalizar")
    public ModelAndView finalizarCompra(HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("compra/finalizada");

        Compra compra = (Compra) session.getAttribute("compra-obj");
        Long clienteId = session.getAttribute("id") != null ? (Long) session.getAttribute("id") : null;
        Cliente cliente = clienteService.obterCliente(clienteId);
        Long carrinhoId = carrinhoCompraService.getOrCreateCart(cliente).getId();
        compra.setCarrinhoId(carrinhoId);

        Compra compraFinalizada = compraService.salvarCompra(compra);
        Long compraId = compraFinalizada.getId();
        String numeroCompra = 20301200 + compraId.toString();
        compraFinalizada.setNumeroCompra(numeroCompra);
        compraService.salvarCompra(compraFinalizada);
        
        List<ItemProduto> itens = carrinhoCompraService.obterCarrinhoCompra(carrinhoId).getItemProduto();
        for (ItemProduto item : itens) {
            item.setCompra(compraFinalizada);
        }
        itemProdutoService.salvarItens(itens);

        String codigoCupom = (String) session.getAttribute("codigoCupom");
        if (codigoCupom != null) {
            Cupom cupom = cupomService.obterCupomByCodigo(codigoCupom);
            cupom.setUsoContador(cupom.getUsoContador() + 1);
            cupomService.salvarCupom(cupom);
        }

        CarrinhoCompra carrinho = carrinhoCompraService.obterCarrinhoCompra(compraFinalizada.getCarrinhoId());
        carrinho.setAtivo(false);
        carrinhoCompraService.salvarCarrinhoCompra(carrinho);
        session.removeAttribute("compra-obj");

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
    public ModelAndView detalharCompra(@RequestParam Long id, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("usr/compra/detail");
        Compra compra = compraService.obterCompra(id);
        logger.debug("Status:::::::: {}", compra.getStatus());
        String status = compra.getStatus().toString();
        mv.addObject("status", status);
        mv.addObject("compra", compra);
        return mv;
    }

    @RequestMapping("/recebido")
    public ModelAndView recebidoCompra(@RequestParam Long id, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("usr/compra/detail");
        Compra compra = compraService.obterCompra(id);
        if(compra.getStatus() == Status.EM_TRANSITO) {
            compra.setStatus(Status.ENTREGUE);
            compraService.salvarCompra(compra);
        }
        logger.debug("Status:::::::: {}", compra.getStatus());
        String status = compra.getStatus().toString();
        mv.addObject("status", status);
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
        // if(compra.getStatus() != Status.ENTREGUE) {
        //     logger.debug("Cliente status:::::::: {}", compra.getStatus());
        //     String nome = cliente.getNomeCompleto();
        //     mv.addObject("nome", nome);

        // }
        Troca troca = new Troca();
        if(status.equals("EM_TROCA") || status.equals("TROCA_AUTORIZADA") || status.equals("ENVIADO") || status.equals("TROCADO")) {
            troca = trocaService.obterTrocaByCompraId(id);
            mv.addObject("troca", troca);
        } 
        return mv;
    }

    @RequestMapping("/detalharAdmin")
    public ModelAndView detalharCompraAdmin(@RequestParam Long id, HttpSession session) throws Exception {
        return respostaDetalhesCompraAdmin(id);
    }

    @RequestMapping("/aprovar")
    public ModelAndView aprovarCompraADM(@RequestParam Long id, HttpSession session) throws Exception {
        Compra compra = compraService.obterCompra(id);
        if(compra.getStatus() == Status.EM_PROCESSAMENTO) {
            compra.setStatus(Status.APROVADO);
            compraService.salvarCompra(compra);
        }
        return respostaDetalhesCompraAdmin(id);
    }

    @RequestMapping("/enviar") 
    public ModelAndView enviarCompraADM(@RequestParam Long id, HttpSession session) throws Exception {
        Compra compra = compraService.obterCompra(id);
        if(compra.getStatus() == Status.APROVADO) {
            compra.setStatus(Status.EM_TRANSITO);
            compraService.salvarCompra(compra);
        }
        return respostaDetalhesCompraAdmin(id);
    }

    @RequestMapping("/entregue")
    public ModelAndView entregarCompraADM(@RequestParam Long id, HttpSession session) throws Exception {
        Compra compra = compraService.obterCompra(id);
        compra.setStatus(Status.ENTREGUE);
        compraService.salvarCompra(compra);
        return respostaDetalhesCompraAdmin(id);
    }

    @RequestMapping("/cancelar")
    public ModelAndView cancelarCompraADM(@RequestParam Long id, HttpSession session) throws Exception {
        Compra compra = compraService.obterCompra(id);
        compra.setStatus(Status.CANCELADO);
        compraService.salvarCompra(compra);
        return respostaDetalhesCompraAdmin(id);
    }

    @RequestMapping("/trocar")
    public ModelAndView trocarCompra(@RequestParam Long id, HttpSession session) throws Exception {
        Compra compra = compraService.obterCompra(id);
        List<ItemProduto> itens = compra.getItens();
        ModelAndView mv = new ModelAndView("usr/compra/troca");
        mv.addObject("itens", itens);
        logger.debug("Lista Itens:::::::: {}", itens.size());
        return mv;
    }

    @RequestMapping("/trocarSelecionados")
    public ModelAndView trocarSelecionados(@RequestParam("selectedItems") List<Long> id, HttpSession session) throws Exception {
        logger.debug("Lista produtos selecionados:::::::: {}", id.size());
        Long idCompra = null;
        for(Long idz : id) {
            logger.debug("Nome do produto selecionado:::::::: {}", itemProdutoService.obterItemProduto(idz));
            idCompra = itemProdutoService.obterItemProduto(idz).getCompra().getId();
        }
        //Long idCliente = (Long) session.getAttribute("id");
        ModelAndView mv = new ModelAndView("usr/compra/solicitacaoTroca");
        Compra compra = compraService.obterCompra(idCompra);
        logger.debug("Compra status:::::::: {}", compra.getStatus());
        if(compra.getStatus() == Status.ENTREGUE) {
            System.out.println("Chegou até aqui!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            compra.setStatus(Status.EM_TROCA);
            compra = compraService.salvarCompra(compra);
            List<Produto> produtos = new ArrayList<Produto>();
            List<ItemTroca> itensTroca = new ArrayList<ItemTroca>();
            Double valor = 0.0;
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
                itemTroca.setTrocado(false);
                valor += itemTroca.getValor();
                itensTroca.add(itemTroca);
            }
    
            Troca troca = trocaService.obterTrocaByCompraId(compra.getId());
            if(troca == null) {
                LocalDate data = LocalDate.now();
        
                troca = new Troca();
                troca.setCliente(compra.getCliente());
                troca.setData(data);
                troca.setStatus(Status.EM_TROCA);
                troca.setCompra(compra);
                troca.setValorTroca(valor);
                troca = trocaService.salvarTroca(troca);
                
            }
            mv.addObject("troca", troca);
            
            for(ItemTroca itemTroca : itensTroca) {
                itemTroca.setTroca(troca);
                itemTroca.setTrocado(false);
                itemTrocaService.salvarItemTroca(itemTroca);
            }
            mv.addObject("itensTroca", itensTroca);
        }
        return mv;
    }
    
    @RequestMapping("/aceitar")
    public ModelAndView aceitarTrocaADM(@RequestParam Long id, HttpSession session) throws Exception {
        Troca troca = trocaService.obterTroca(id);
        Compra compra = troca.getCompra();
        if(compra.getStatus() == Status.EM_TROCA) {
            compra.setStatus(Status.TROCA_AUTORIZADA);
            compra = compraService.salvarCompra(compra);
            troca.setStatus(Status.TROCA_AUTORIZADA);
            List<ItemTroca> listaItensTroca = itemTrocaService.obterTrocaByCompraIdTrocado(troca.getId(), false);
            Double valor = 0.0;
            for(ItemTroca item : listaItensTroca) {
                valor += item.getValor();
            }
            troca.setValorTroca(valor);
            troca = trocaService.salvarTroca(troca);
        }
        //Cliente cliente = compra.getCliente();
        //List<ItemTroca> itens = troca.getItemTroca();
        
        //Double valor = troca.calculoValorTroca(itens);
        Long compraId = compra.getId();
        return respostaDetalhesCompraAdmin(compraId);
    }
    
    @RequestMapping("/recusar")
    public ModelAndView recusarTrocaADM(@RequestParam Long id, HttpSession session) throws Exception {
        Compra compra = compraService.obterCompra(id);
        if(compra.getStatus() == Status.EM_TROCA) {
            compra.setStatus(Status.REPROVADO);
            compra = compraService.salvarCompra(compra);
        }
        return respostaDetalhesCompraAdmin(id);
    }

    @RequestMapping("/enviado")
    public ModelAndView enviadoTroca(@RequestParam Long id, HttpSession session) throws Exception {
        Compra compra = compraService.obterCompra(id);
        if(compra.getStatus() == Status.TROCA_AUTORIZADA) {
            logger.debug("Compra status:::::::: {}", compra.getStatus());
            compra.setStatus(Status.ENVIADO);
            compra = compraService.salvarCompra(compra);
        }
        List<ItemProduto> itens = compra.getItens();
        ModelAndView mv = new ModelAndView("usr/compra/detail");
        mv.addObject("itens", itens);
        mv.addObject("compra", compra);
        logger.debug("lista de itens enviados:::::::: {}", itens.size());
        return mv;
    }

    @RequestMapping("/recebidoTroca")
    public ModelAndView recebidoTrocaADM(@RequestParam Long id, HttpSession session) throws Exception {
        Troca troca = trocaService.obterTroca(id);
        Cliente cliente =  troca.getCliente();
        
        Compra compra = troca.getCompra();
        
        compra.setStatus(Status.TROCADO);
        compraService.salvarCompra(compra);
        
        troca.setStatus(Status.TROCADO);
        troca = trocaService.salvarTroca(troca);

        List<ItemTroca> lista = troca.getItemTroca();
        for(ItemTroca item : lista) {
            if(item.getTrocado() == false) {
                item.setTrocado(true);
                itemTrocaService.salvarItemTroca(item);
            }
        }

        Double valor = troca.getValorTroca();

        Cupom cupom = cupomService.gerarCupomTroca(cliente, compra, troca, valor);

        logger.debug("Cupom value:::::::: {}", cupom.getValor());

        Long compraId = compra.getId();

        return respostaDetalhesCompraAdmin(compraId);
    }

    @GetMapping("/comprasPorMes")
    public String comprasPorMes(Model model) {


        // Obter dados agregados de compras por mês
        Map<String, Long> comprasPorMes = compraService.getComprasPorMes();

        // Lista com a ordem correta dos meses
        List<String> monthOrder = Arrays.asList(
                "01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12"
        );

        // Mapa para armazenar os dados ordenados com todos os meses
        Map<String, Long> sortedComprasPorMes = new LinkedHashMap<>();

        // Preencher o mapa ordenado com todos os meses e valores de compras (ou zero se não houver compras)
        for (String month : monthOrder) {
            sortedComprasPorMes.put(month, comprasPorMes.getOrDefault(month, 0L));
            logger.debug("Month:::::::: {}", month);
            logger.debug("compras::::::::::::: {}", comprasPorMes.getOrDefault(month, 0L));
        }

        // Adiciona dados ordenados ao modelo para serem usados no template
        model.addAttribute("comprasLabels", sortedComprasPorMes.keySet());
        model.addAttribute("comprasData", sortedComprasPorMes.values());

        Map<String, Map<String, Integer>> detalhamentoComprasPorMes = compraService.getDetalhamentoComprasPorMes();
        model.addAttribute("detalhamentoComprasPorMes", detalhamentoComprasPorMes);
        logger.debug("Detalhamento:::::::::::::::::::::::::::: {}", detalhamentoComprasPorMes);

        // Nome do template Thymeleaf para exibir o gráfico
        return "adm/main/barchart";
    }
    
}
