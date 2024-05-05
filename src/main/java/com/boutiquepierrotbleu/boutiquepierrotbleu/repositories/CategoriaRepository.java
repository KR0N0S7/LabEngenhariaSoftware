package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ValorCategoria;

public interface CategoriaRepository extends JpaRepository<ValorCategoria, Long>{
    
}
