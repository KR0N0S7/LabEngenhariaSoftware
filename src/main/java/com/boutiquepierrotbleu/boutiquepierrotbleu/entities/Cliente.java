package com.boutiquepierrotbleu.boutiquepierrotbleu.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeCompleto;
    private String email;
    private String cpf;
    private String telefone;
    private String senha;

    @OneToMany(mappedBy = "cliente")
    private List<Endereco> endereco;

    @OneToMany(mappedBy = "cliente")
    private List<CarrinhoCompra> carrinhoCompra;

    @OneToMany(mappedBy = "cliente")
    private List<Creditcard> creditcard;

    @OneToMany(mappedBy = "cliente")
    private List<Compra> compra;

    @OneToMany(mappedBy = "cliente")
    private List<Cupom> cupom;

    private boolean ativo = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Endereco> getEndereco() {
        return endereco;
    }

    public void setEndereco(List<Endereco> endereco) {
        this.endereco = endereco;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<CarrinhoCompra> getCarrinhoCompra() {
        return carrinhoCompra;
    }

    public void setCarrinhoCompra(List<CarrinhoCompra> carrinhoCompra) {
        this.carrinhoCompra = carrinhoCompra;
    }

    public List<Creditcard> getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(List<Creditcard> creditcard) {
        this.creditcard = creditcard;
    }

    public List<Compra> getCompra() {
        return compra;
    }

    public void setCompra(List<Compra> compra) {
        this.compra = compra;
    }

    public List<Cupom> getCupom() {
        return cupom;
    }

    public void setCupom(List<Cupom> cupom) {
        this.cupom = cupom;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}

