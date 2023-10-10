package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Creditcard;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.CreditcardRepository;

@Service
public class CreditcardService {
    @Autowired
    private CreditcardRepository creditcardRepository;

    public Creditcard salvarCreditcard(Creditcard creditcard) {
        return creditcardRepository.save(creditcard);
    }

    public Creditcard obterCreditcard(Long id) throws Exception {
        Optional<Creditcard> creditcard = creditcardRepository.findById(id);
        if (creditcard.isPresent()) {
            return creditcard.get();
        }
        throw new Exception("Creditcard não encontrado");
    }

    public List<Creditcard> listarCreditcards() {
        return creditcardRepository.findAll();
    }

    public void excluirCreditcard(Long id) {
        creditcardRepository.deleteById(id);
    }

    public boolean validarDetalhesDoCartao(String numeroCartao, String dataValidade, String codigoSeguranca) {
        // Placeholder method to validate card details using payment gateway API
        // Integrate with real payment gateway API to validate card details here
        return true;
    }

    public boolean processarPagamento(Double valor, Integer parcelas) {
        // Placeholder method to process payment using payment gateway API
        // Integrate with real payment gateway API to process payment here
        return true;
    }
}
