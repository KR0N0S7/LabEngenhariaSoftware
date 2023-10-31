package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemProduto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ValorCategoria;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CarrinhoCompraService;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CategoriaService;
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
    
    @Autowired
    private CategoriaService categoriaService;

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    @RequestMapping("listarProdutos")
    public ModelAndView listarProdutos(HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("produto/page");
        List<Produto> produtos = produtoService.listarProdutos();
        mv.addObject("produtos", produtos);
        Long idCliente = (Long) session.getAttribute("id");
        mv.addObject("id", idCliente);

        Cliente cliente = clienteService.obterCliente(idCliente);
        Integer numProdutosNoCarrinho = carrinhoCompraService.getOrCreateCart(cliente).getItemProduto().stream()
                .mapToInt(ItemProduto::getQuantidade)
                .sum();;
        mv.addObject("numeroProdutos", numProdutosNoCarrinho);
        logger.debug("Session id: {}", session.getId());
        logger.debug("Session id: {}", session.getAttribute("id"));
        logger.debug("Nome do cliente: {}", cliente.getNomeCompleto());
        logger.debug("Numero de produtos: {}", numProdutosNoCarrinho);
        return mv;
    }

    @RequestMapping(value = "/detalhar", method = RequestMethod.GET)
    public ModelAndView detalharProduto(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping("/criar")
    public ModelAndView criarProduto(@RequestParam(required = false) Long id) {
        ModelAndView mv = new ModelAndView("adm/produto/novo");
		Produto produto;
		if (id == null) {
			produto = new Produto();
		} else {
			try {
				produto = produtoService.obterProduto(id);
			} catch (Exception e) {
				produto = new Produto();
				mv.addObject("mensagem", e.getMessage());
			}
		}
		mv.addObject("produto", produto);

        List<ValorCategoria> lista = categoriaService.getCategorias();
        mv.addObject("lista", lista);
		return mv;
    }

    @RequestMapping(method = RequestMethod.POST, path = "editar")
	public ModelAndView produtoSalvo(Produto produto, BindingResult bidingResult, RedirectAttributes redirectAttributes) {
		if (bidingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("produto/criar");
			mv.addObject("produto", produto);
			return mv;
		}
		ModelAndView mv = new ModelAndView("redirect:/produto/criar");
		boolean novo = true;
		if (produto != null) {
			novo = false;
		}
		if (novo) {
			mv.addObject("produto", new Produto());
		} else {
            mv.addObject("produto", produto);
		}
        if (produto != null) {
            Float porcentagem = produto.getValorCategoria().getPorcentagem();
            Double valor = (1.00 + (porcentagem * 0.01)) * produto.getCusto();

            BigDecimal bd = new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP);
            Double roundedValue = bd.doubleValue();

            produto.setPreco(roundedValue);
            logger.debug("Porcentagem::::::::::::::: {}", produto.getValorCategoria());
        }
        produtoService.salvarProduto(produto);

        mv.addObject("mensagem", "Produto salvo com sucesso!"); 
		redirectAttributes.addFlashAttribute("produtoCreatedSuccess", true);
		return mv;
	}

    @RequestMapping(value = "estoque", method = RequestMethod.GET)
    public ModelAndView atualizarEstoqueProduto(@RequestParam(required = false) Long id) {
        ModelAndView mv = new ModelAndView("adm/produto/estoque");
        
        Produto produto;
		try {
            produto = produtoService.obterProduto(id);
            String nome = produto.getNome();
            mv.addObject("nome", nome);
            //mv.addObject("produto", produto);
        } catch (Exception e) {
            produto = new Produto();
            mv.addObject("mensagem", e.getMessage());
        }

        Integer quantidade = 0;
        mv.addObject("quatidade", quantidade);
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping(value = "atualizar", method = RequestMethod.POST)
    public ModelAndView reservarEstoqueDeProduto(@RequestParam("id")Long id, @RequestParam("quantidade") Integer quantidade) {
        ModelAndView mv = new ModelAndView("adm/produto/list");
        Produto produto;
		try {
            produto = produtoService.obterProduto(id);
            
        } catch (Exception e) {
            produto = new Produto();
            mv.addObject("mensagem", e.getMessage());
        }
        Integer estoqueAntigo = produto.getEstoque();
        produto.setEstoque(estoqueAntigo + quantidade);
        produtoService.salvarProduto(produto);
        mv.addObject("lista", produtoService.listarProdutos());
        mv.addObject("mensagem", "Estoque atualizado com sucesso!");
        return mv;
    }
    
    @RequestMapping(value = "/deletar", method = RequestMethod.GET)
    public ModelAndView deletarProduto(Long id) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @RequestMapping(value = "/liberar", method = RequestMethod.GET)
    public ModelAndView liberarEstoqueDeProduto(Long id, Integer quantity) {
        ModelAndView mv = new ModelAndView("path/to/your/view");
        // Your implementation here
        return mv;
    }

    @PostMapping("/updateAtivo")
    public ModelAndView updateAtivoStatus(@RequestParam("produtoId") Long produtoId, RedirectAttributes attributes) {
        ModelAndView mv = new ModelAndView("redirect:/produto/list");
        produtoService.toggleAtivoStatusById(produtoId);
        attributes.addFlashAttribute("message", "Status atualizado com sucesso!");
        return mv;
    }

    @RequestMapping("/list")
    public ModelAndView listarTodosProdutos() {
        ModelAndView mv = new ModelAndView("adm/produto/list");
        
        mv.addObject("lista", produtoService.listarProdutos());
        
        return mv;
    }
}
