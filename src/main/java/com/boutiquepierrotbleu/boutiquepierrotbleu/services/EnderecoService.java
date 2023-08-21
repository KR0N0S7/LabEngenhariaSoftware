package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Endereco;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.EnderecoRepository;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository enderecoRepository;

    public Endereco salvarEndereco(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public Endereco obterEndereco(Long id) throws Exception {
        Optional<Endereco> endereco = enderecoRepository.findById(id);
        if (endereco.isEmpty()) {
            throw new Exception("Endereco não existe!");
        }
        return endereco.get();
    }

    public List<Endereco> obterTodosEnderecos() {
        return enderecoRepository.findAll();
    }

    public void excluirEndereco(Long id) {
        enderecoRepository.deleteById(id);
    }
}
