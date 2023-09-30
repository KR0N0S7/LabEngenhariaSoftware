package com.boutiquepierrotbleu.boutiquepierrotbleu.dto;

import java.util.List;

public class ClienteSearchCriteria {
    private Long id;
    private String nomeCompleto;
    private String email;
    private String cpf;
    private String telefone;
    private List<String> endereco;
    private List<String> carrinhoCompra;
    private List<String> creditcard;
    private List<String> compra;
    private List<String> cupom;
    private boolean ativo;

    public ClienteSearchCriteria() {
    }

    public ClienteSearchCriteria(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public ClienteSearchCriteria(Long id, String nomeCompleto, String email, String cpf, String telefone) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public ClienteSearchCriteria(Long id, String nomeCompleto, String email, String cpf, String telefone,
            List<String> endereco, List<String> carrinhoCompra, List<String> creditcard, List<String> compra,
            List<String> cupom, boolean ativo) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
        this.carrinhoCompra = carrinhoCompra;
        this.creditcard = creditcard;
        this.compra = compra;
        this.cupom = cupom;
        this.ativo = ativo;
    }
    
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
    public List<String> getEndereco() {
        return endereco;
    }
    public void setEndereco(List<String> endereco) {
        this.endereco = endereco;
    }
    public List<String> getCarrinhoCompra() {
        return carrinhoCompra;
    }
    public void setCarrinhoCompra(List<String> carrinhoCompra) {
        this.carrinhoCompra = carrinhoCompra;
    }
    public List<String> getCreditcard() {
        return creditcard;
    }
    public void setCreditcard(List<String> creditcard) {
        this.creditcard = creditcard;
    }
    public List<String> getCompra() {
        return compra;
    }
    public void setCompra(List<String> compra) {
        this.compra = compra;
    }
    public List<String> getCupom() {
        return cupom;
    }
    public void setCupom(List<String> cupom) {
        this.cupom = cupom;
    }
    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "ClientSearchCriteria [id=" + id + ", nomeCompleto=" + nomeCompleto + ", email=" + email + ", cpf=" + cpf
                + ", telefone=" + telefone + ", endereco=" + endereco + ", carrinhoCompra=" + carrinhoCompra
                + ", creditcard=" + creditcard + ", compra=" + compra + ", cupom=" + cupom + ", ativo=" + ativo + "]";
    }

    
}
