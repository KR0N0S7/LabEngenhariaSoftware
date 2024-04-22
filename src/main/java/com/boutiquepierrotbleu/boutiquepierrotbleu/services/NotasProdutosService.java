package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.NotasProdutos;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.NotasProdutosRepository;

@Service
public class NotasProdutosService {
    @Autowired
    private NotasProdutosRepository notasProdutosRepository;

    public NotasProdutos salvarNotaProduto(NotasProdutos notaProduto) {
        return notasProdutosRepository.save(notaProduto);
    }

    public NotasProdutos obterNotaProduto(Long id) throws Exception {
        Optional<NotasProdutos> notaProduto = notasProdutosRepository.findById(id);
        if (notaProduto.isEmpty()) {
            throw new Exception("Nota do produto não encontrada!");
        }
        return notaProduto.get();
    }

    public List<NotasProdutos> listarNotasProdutos() {
        return notasProdutosRepository.findAll();
    }

    public void deletarNotaProduto(Long id) {
        notasProdutosRepository.deleteById(id);
    }
}
