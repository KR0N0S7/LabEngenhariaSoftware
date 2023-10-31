package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Troca;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.TrocaRepository;

@Service
public class TrocaService {
    @Autowired
    private TrocaRepository trocaRepository;

    public Troca salvarTroca(Troca troca) {
        return trocaRepository.save(troca);
    }
}
