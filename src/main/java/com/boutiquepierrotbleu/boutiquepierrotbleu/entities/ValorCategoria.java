package com.boutiquepierrotbleu.boutiquepierrotbleu.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class ValorCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoria;
    private Float porcentagem;

    @OneToMany(mappedBy = "categoria")
    @JsonBackReference
    private List<Produto> produto;

    @OneToMany(mappedBy = "categoria")
    @JsonBackReference
    private List<NotasProdutos> notasProdutos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Float getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Float porcentagem) {
        this.porcentagem = porcentagem;
    }

    public List<Produto> getProduto() {
        return produto;
    }

    public void setProduto(List<Produto> produto) {
        this.produto = produto;
    }

    public List<NotasProdutos> getNotasProdutos() {
        return notasProdutos;
    }

    public void setNotasProdutos(List<NotasProdutos> notasProdutos) {
        this.notasProdutos = notasProdutos;
    }

}
