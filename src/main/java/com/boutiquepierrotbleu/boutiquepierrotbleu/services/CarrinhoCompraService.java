package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.CarrinhoCompra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemProduto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.exceptions.InsufficientStockException;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.CarrinhoCompraRepository;

@Service
public class CarrinhoCompraService {
    @Autowired
    private CarrinhoCompraRepository carrinhoCompraRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ItemProdutoService itemProdutoService;

    public CarrinhoCompra salvarCarrinhoCompra(CarrinhoCompra carrinhoCompra) {
        return carrinhoCompraRepository.save(carrinhoCompra);
    }

    public CarrinhoCompra obterCarrinhoCompra(Long id) throws Exception {
        Optional<CarrinhoCompra> carrinho = carrinhoCompraRepository.findById(id);
        if (carrinho.isEmpty()) {
            throw new Exception("Carrinho não existe!");
        }
        return carrinho.get();
    }

    public List<CarrinhoCompra> listarCarrinhos() {
        return carrinhoCompraRepository.findAll();
    }

    public void excluirCarrinhoCompra(Long id) {
        carrinhoCompraRepository.deleteById(id);
    }

    @Transactional
    public void adicionarItemAoCarrinho(CarrinhoCompra carrinho, Long produtoId, Integer quantity) throws Exception {
        Produto produto = produtoService.obterProduto(produtoId); // This method should throw an EntityNotFoundException if not found

        if(produto.isStockAvailable(quantity)) {
            produto.reserveStock(quantity); // This method should update the estoque and save the entity

            ItemProduto item = new ItemProduto();
            item.setProduto(produto);
            item.setQuantidade(quantity);
            item.setPreco(produto.getPreco()); // Ensure the price is set based on the current product price
            
            carrinho.addItemProduto(item);
            carrinhoCompraRepository.save(carrinho);
        } else {
            throw new InsufficientStockException("Not enough stock for product: " + produto.getNome());
        }
    }


    @Transactional
    public void removerItemDoCarrinho(Long carrinhoId, Long itemId) throws Exception {
        CarrinhoCompra carrinho = obterCarrinhoCompra(carrinhoId); // This method should throw an EntityNotFoundException if not found
        ItemProduto item = itemProdutoService.obterItemProduto(itemId); // This method should throw an EntityNotFoundException if not found
        
        Produto produto = item.getProduto(); // Assuming that item has a getProduto() method
        produto.increaseEstoque(item.getQuantidade()); // This method should update the estoque and save the entity
        
        carrinho.removeItemProduto(item);
        carrinhoCompraRepository.save(carrinho);
    }

    public CarrinhoCompra getOrCreateCart(Cliente cliente) {
        // Retrieve existing cart or create a new one
        return carrinhoCompraRepository.findByCliente(cliente)
                                      .orElseGet(() -> {
                                          CarrinhoCompra newCart = new CarrinhoCompra();
                                          newCart.setCliente(cliente);
                                          return carrinhoCompraRepository.save(newCart);
                                      });
    }

    public void addItemProdutoToCart(Cliente cliente, ItemProduto itemProduto) {
        CarrinhoCompra carrinho = getOrCreateCart(cliente);
        carrinho.addItemProduto(itemProduto);
        // Ensure the itemProduto is saved and associated with Produto...
        carrinhoCompraRepository.save(carrinho);
    }

}
