package com.boutiquepierrotbleu.boutiquepierrotbleu.entities;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Cupom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String tipo; // Tipo can be "PERCENTAGE" or "FIXED_AMOUNT"
    private Double valor; // New attribute to represent the discount value (percentage or fixed amount)
    private Date dataValidade;
    private Integer usoLimite; // New attribute to represent the usage limit of the coupon
    private Integer usoContador; // New attribute to represent the usage counter of the coupon

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "compra_id")
    private Compra compra;

    @OneToOne(mappedBy = "cupom")
    private Troca troca;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public boolean isValid() {
        return dataValidade.after(new Date()) && (usoLimite == null || usoContador < usoLimite);
    }
    
    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getUsoLimite() {
        return usoLimite;
    }
    
    public void setUsoLimite(Integer usoLimite) {
        this.usoLimite = usoLimite;
    }

    public Integer getUsoContador() {
        return usoContador;
    }

    public void setUsoContador(Integer usoContador) {
        this.usoContador = usoContador;
    }

    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Troca getTroca() {
        return troca;
    }

    public void setTroca(Troca troca) {
        this.troca = troca;
    }
    
    public Double aplicarDesconto(Double valorOriginal) {
        if (isValid()) {
            if ("PERCENTAGE".equals(tipo)) {
                return valorOriginal * valor / 100;
            } else if ("FIXED_AMOUNT".equals(tipo)) {
                return valor;
            }
        }
        return 0.0;
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString();
    }

    public Cupom gerarCupom(Cliente cliente, Compra compra, String tipo, Double valor) {
        // Logic to generate a unique coupon code
        String uniqueCode = generateUniqueCode();
        
        // Create a new Coupon object
        Cupom novoCupom = new Cupom();
        novoCupom.setCodigo(uniqueCode);
        novoCupom.setCliente(cliente);  // assuming a bidirectional relationship
        novoCupom.setCompra(compra);    // assuming a bidirectional relationship
        novoCupom.setUsoContador(0);
        novoCupom.setUsoLimite(1);
        novoCupom.setTipo(tipo);
        novoCupom.setValor(valor);

        return novoCupom;
    }

    
}
