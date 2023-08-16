package com.boutiquepierrotbleu.boutiquepierrotbleu.entities;

import jakarta.persistence.Entity;

@Entity
public class Cliente {
    private Long id;
    private String nomeCompleto;
    private String email;
    private String cpf;
    private String telefone;
    private Endereco endereco;
    private String senha;

}
