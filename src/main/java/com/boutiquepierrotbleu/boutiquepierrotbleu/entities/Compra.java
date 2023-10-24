package com.boutiquepierrotbleu.boutiquepierrotbleu.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String data;
    private String hora;
    private Status status;
    private Pagamento formaPagamento;
    private Double valorTotal;
    private Double valorFrete;
    private Double valorDesconto;
    private Double valorFinal;
    private String observacao;
    private String numeroCompra;
    private Integer numeroParcelas;

    @OneToMany(mappedBy = "compra")
    private List<Creditcard> cartao;

    @OneToMany(mappedBy = "compra")
    private List<Cupom> cupons;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco enderecoEntrega;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "compra")
    private List<ItemProduto> itens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Pagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(Pagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public String getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(String numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Integer getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(Integer numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(Double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public Double getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(Double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public List<ItemProduto> getItens() {
        return itens;
    }

    public void setItens(List<ItemProduto> itens) {
        this.itens = itens;
    }

    public List<Cupom> getCupons() {
        return cupons;
    }

    public void setCupons(List<Cupom> cupons) {
        this.cupons = cupons;
    }

    public List<Creditcard> getCartao() {
        return cartao;
    }

    public void setCartao(List<Creditcard> cartao) {
        this.cartao = cartao;
    }

    public Compra(CarrinhoCompra carrinho) {
        this.cliente = carrinho.getCliente();
        this.itens = carrinho.getItemProduto();
        this.formaPagamento = null;
        this.numeroParcelas = 0;
        this.valorTotal = carrinho.getValorTotal();
        this.data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.valorFrete = 0.0;
        this.valorDesconto = 0.0;
        this.valorFinal = this.valorTotal + this.valorFrete - this.valorDesconto;
        this.status = Status.AGUARDANDO_PAGAMENTO;
        this.observacao = "";
        this.numeroCompra = "";
        this.enderecoEntrega = null;
        this.cartao = null;
    }

    public Compra() {
    }
}
