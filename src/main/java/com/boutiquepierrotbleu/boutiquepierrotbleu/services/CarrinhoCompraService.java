package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.CarrinhoCompra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemProduto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.CarrinhoCompraRepository;

@Service
public class CarrinhoCompraService {
    @Autowired
    private CarrinhoCompraRepository carrinhoCompraRepository;

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
    public void adicionarItemAoCarrinho(Long carrinhoId, ItemProduto item) throws Exception {
        CarrinhoCompra carrinho = obterCarrinhoCompra(carrinhoId);
        carrinho.addItemProduto(item);
        carrinhoCompraRepository.save(carrinho);
    }

    @Transactional
    public void removerItemDoCarrinho(Long carrinhoId, ItemProduto item) throws Exception {
        CarrinhoCompra carrinho = obterCarrinhoCompra(carrinhoId);
        carrinho.removeItemProduto(item);
        carrinhoCompraRepository.save(carrinho);
    }
}
