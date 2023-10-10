package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cupom;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.CupomRepository;

@Service
public class CupomService {
    @Autowired
    private CupomRepository cupomRepository;

    public Cupom salvarCupom(Cupom cupom) {
        return cupomRepository.save(cupom);
    }

    public Cupom obterCupom(Long id) throws Exception {
        Optional<Cupom> cupom = cupomRepository.findById(id);
        if (cupom.isEmpty()) {
            throw new Exception("Cupom não existe!");
        }
        return cupom.get();
    }

    public List<Cupom> listarCupons() {
        return cupomRepository.findAll();
    }

    public void excluirCupom(Long id) {
        cupomRepository.deleteById(id);
    }

    public Double aplicarCupom(Long id, Double valorOriginal) throws Exception {
        Cupom cupom = obterCupom(id);
        return cupom.aplicarDesconto(valorOriginal);
    }
}
