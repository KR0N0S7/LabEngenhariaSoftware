package com.boutiquepierrotbleu.boutiquepierrotbleu.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
}
