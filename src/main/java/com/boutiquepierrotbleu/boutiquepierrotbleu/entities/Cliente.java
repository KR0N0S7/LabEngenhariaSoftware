package com.boutiquepierrotbleu.boutiquepierrotbleu.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    private Boolean logged;

    @OneToMany(mappedBy = "cliente")
    @JsonManagedReference // evita loop infinito cliente>enderecos>cliente>enderecos>... 
    // net::ERR_INCOMPLETE_CHUNKED_ENCODING 200
    // Caused by: org.thymeleaf.exceptions.TemplateProcessingException: An exception was raised while trying to serialize object to JavaScript using Jackson
	// at org.thymeleaf.standard.serializer.StandardJavaScriptSerializer$JacksonStandardJavaScriptSerializer.serializeValue(StandardJavaScriptSerializer.java:190) ~[thymeleaf-3.1.2.RELEASE.jar:3.1.2.RELEASE]
    // Caused by: com.fasterxml.jackson.databind.JsonMa
    private List<Endereco> enderecos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<CarrinhoCompra> carrinhoCompra;

    @OneToMany(mappedBy = "cliente")
    @JsonManagedReference
    private List<Creditcard> creditcard;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Compra> compra;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Cupom> cupom;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Troca> trocas;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<ItemTroca> itemTroca;   

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

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEndereco(List<Endereco> enderecos) {
        this.enderecos = enderecos;
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

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean logged) {
        this.logged = logged;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public List<Troca> getTrocas() {
        return trocas;
    }

    public void setTrocas(List<Troca> trocas) {
        this.trocas = trocas;
    }

    public List<ItemTroca> getItemTroca() {
        return itemTroca;
    }

    public void setItemTroca(List<ItemTroca> itemTroca) {
        this.itemTroca = itemTroca;
    }

    
}

