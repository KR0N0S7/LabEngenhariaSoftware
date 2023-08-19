package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.CarrinhoCompra;
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
}
