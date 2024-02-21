package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemTroca;

public interface ItemTrocaRepository extends JpaRepository<ItemTroca, Long>{

    List<ItemTroca> findByTrocaIdAndTrocado(Long trocaId, boolean trocado);
    
}
