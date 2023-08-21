package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemProduto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.ItemProdutoRepository;

@Service
public class ItemProdutoService {
    @Autowired
    private ItemProdutoRepository itemProdutoRepository;

    public ItemProduto salvaItemProduto(ItemProduto itemProduto) {
        return itemProdutoRepository.save(itemProduto);
    }

    public ItemProduto obterItemProduto(Long id) throws Exception {
        Optional<ItemProduto> itemProduto = itemProdutoRepository.findById(id);
        if (itemProduto.isEmpty()) {
            throw new Exception("ItemProduto não encontrado!");
        }
        return itemProduto.get();
    }

    public List<ItemProduto> listarItemProdutos() {
        return itemProdutoRepository.findAll();
    }

    public void deletarItemProduto(Long id) {
        itemProdutoRepository.deleteById(id);
    }
}
