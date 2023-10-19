package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.CarrinhoCompra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Compra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.CompraRepository;

@Service
public class CompraService {
    @Autowired
    private CompraRepository compraRepository;

    public Compra salvarCompra(Compra compra) {
        return compraRepository.save(compra);
    }

    public Compra obterCompra(Long id) throws Exception {
        Optional<Compra> compra = compraRepository.findById(id);
        if (compra.isEmpty()) {
            throw new Exception("Compra não existe!");
        }
        return compra.get();
    }

    public List<Compra> listarCompras() {
        return compraRepository.findAll();
    }

    public void deletarCompra(Long id) {
        compraRepository.deleteById(id);
    }

}
