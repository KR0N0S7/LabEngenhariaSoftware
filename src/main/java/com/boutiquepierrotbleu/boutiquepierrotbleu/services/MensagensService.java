package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Mensagens;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.MensagensRepository;

@Service
public class MensagensService {
    @Autowired
    private MensagensRepository mensagensRepository;

    public Mensagens salvarMensagem(Mensagens mensagem) {
        return mensagensRepository.save(mensagem);
    }

    public Mensagens obterMensagem(Long id) throws Exception {
        Optional<Mensagens> mensagem = mensagensRepository.findById(id);
        if (mensagem.isEmpty()) {
            throw new Exception("Mensagem não existe!");
        }
        return mensagem.get();
    }

    public List<Mensagens> listarMensagens() {
        return mensagensRepository.findAll();
    }

    public void excluirMensagem(Long id) {
        mensagensRepository.deleteById(id);
    }
}
