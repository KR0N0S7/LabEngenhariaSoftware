package com.boutiquepierrotbleu.boutiquepierrotbleu.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class CarrinhoCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double valorTotal;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "carrinhoCompra")
    private List<ItemProduto> itemProduto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemProduto> getItemProduto() {
        return itemProduto;
    }

    public void setItemProduto(List<ItemProduto> itemProduto) {
        this.itemProduto = itemProduto;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void addItemProduto(ItemProduto item) {
        if (item.getProduto().isStockAvailable(item.getQuantidade())) {
            item.getProduto().reserveStock(item.getQuantidade());
            itemProduto.add(item);
            calcularValorTotal();
        } else {
            throw new RuntimeException("Insufficient stock available");
        }
    }

    public void removeItemProduto(ItemProduto item) {
        if (itemProduto.remove(item)) {
            item.getProduto().releaseStock(item.getQuantidade());
            calcularValorTotal();
        }
    }

    public void calcularValorTotal() {
        valorTotal = itemProduto.stream()
                .mapToDouble(item -> item.getQuantidade() * item.getProduto().getPreco())
                .sum();
    }
}
