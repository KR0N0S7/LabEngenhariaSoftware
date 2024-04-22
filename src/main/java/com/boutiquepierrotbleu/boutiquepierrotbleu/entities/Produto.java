package com.boutiquepierrotbleu.boutiquepierrotbleu.entities;

import java.util.List;

import com.boutiquepierrotbleu.boutiquepierrotbleu.exceptions.InsufficientStockException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Double custo;
    private String imagem;
    private String tamanho;
    private String cor;
    private String marca;
    private String genero;
    private String material;
    private String tipo;
    private String estilo;
    private Integer estoque;
    private boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonManagedReference
    private ValorCategoria categoria;

    @OneToMany(mappedBy = "produto")
    @JsonIgnore
    private List<ItemProduto> itemProduto;

    @OneToMany(mappedBy = "produto")
    @JsonIgnore
    private List<ItemTroca> itemTrocas;

    @OneToMany(mappedBy = "produto")
    @JsonIgnore
    private List<NotasProdutos> notasProdutos;

    public List<ItemProduto> getItemProduto() {
        return itemProduto;
    }

    public void setItemProduto(List<ItemProduto> itemProduto) {
        this.itemProduto = itemProduto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getCusto() {
        return custo;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public ValorCategoria getValorCategoria() {
        return categoria;
    }

    public void setValorCategoria(ValorCategoria categoria) {
        this.categoria = categoria;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public List<ItemTroca> getTroca() {
        return itemTrocas;
    }

    public void setTroca(List<ItemTroca> troca) {
        this.itemTrocas = troca;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public ValorCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ValorCategoria categoria) {
        this.categoria = categoria;
    }

    public List<ItemTroca> getItemTrocas() {
        return itemTrocas;
    }

    public void setItemTrocas(List<ItemTroca> itemTrocas) {
        this.itemTrocas = itemTrocas;
    }

    public List<NotasProdutos> getNotasProdutos() {
        return notasProdutos;
    }

    public void setNotasProdutos(List<NotasProdutos> notasProdutos) {
        this.notasProdutos = notasProdutos;
    }

    // New methods to manage stock
    public boolean isStockAvailable(int requiredQuantity) {
        return this.estoque >= requiredQuantity;
    }

    public void reserveStock(int quantity) {
        if (!isStockAvailable(quantity)) {
            throw new InsufficientStockException("Estoque insuficiente!");
        }
        this.estoque -= quantity;
    }

    public void increaseEstoque(int quantity) {
        this.estoque += quantity;
    }

    public void decreaseEstoque(int quantity) {
        this.estoque -= quantity;
    }
}
