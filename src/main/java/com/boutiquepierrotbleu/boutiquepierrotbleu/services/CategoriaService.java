package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ValorCategoria;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<ValorCategoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    public ValorCategoria salvarCategoria(ValorCategoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public ValorCategoria obterCategoria(Long id) throws Exception {
        Optional<ValorCategoria> categoria = categoriaRepository.findById(id);
        if (categoria.isEmpty()) {
            throw new Exception("Categoria não encontrada!");
        }
        return categoria.get();
    }
    
}
