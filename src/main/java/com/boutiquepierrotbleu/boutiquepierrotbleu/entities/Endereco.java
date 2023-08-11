package com.boutiquepierrotbleu.boutiquepierrotbleu.entities;

import jakarta.persistence.Entity;

@Entity
public class Endereco {
    private Long id;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
}
