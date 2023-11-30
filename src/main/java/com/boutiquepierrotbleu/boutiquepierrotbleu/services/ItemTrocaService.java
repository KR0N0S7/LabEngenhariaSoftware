package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemTroca;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.ItemTrocaRepository;

@Service
public class ItemTrocaService {

    @Autowired
    private ItemTrocaRepository itemTrocaRepository;

    public void salvarItemTroca(ItemTroca itemTroca) {
        itemTrocaRepository.save(itemTroca);
    }
    
}
