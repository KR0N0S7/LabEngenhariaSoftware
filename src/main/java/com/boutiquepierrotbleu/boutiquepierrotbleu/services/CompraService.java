package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.CarrinhoCompra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Compra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ItemProduto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.CompraRepository;

@Service
public class CompraService {
    @Autowired
    private CompraRepository compraRepository;

    public Compra salvarCompra(Compra compra) {
        return compraRepository.save(compra);
    }

    public Compra obterCompra(Long id) throws Exception {
        Optional<Compra> compra = compraRepository.findById(id);
        if (compra.isEmpty()) {
            throw new Exception("Compra não existe!");
        }
        return compra.get();
    }

    public List<Compra> listarCompras() {
        return compraRepository.findAll();
    }

    public void deletarCompra(Long id) {
        compraRepository.deleteById(id);
    }

    public List<Compra> getComprasByClienteId(Long clienteId) {
        return compraRepository.getComprasByClienteId(clienteId);
    }

    public Map<String, Long> getComprasPorMes() {
        List<Map<String, Object>> compras = compraRepository.countComprasPorMes();
    
        return compras.stream()
                      .collect(Collectors.toMap(
                          compra -> "%02d".formatted((Integer) compra.get("mes")), // Garante que o mês seja formatado como "01", "02", etc.
                          compra -> ((Number) compra.get("total")).longValue(), // Valor do mapa: total, convertido para Long
                          (oldValue, newValue) -> oldValue // Em caso de chaves duplicadas, mantenha o valor antigo
                      ));
    }

    public Map<String, Map<String, Integer>> getDetalhamentoComprasPorMes() {
        // Este mapa vai armazenar o resultado final com a estrutura mês -> (produto -> quantidade)
        Map<String, Map<String, Integer>> detalhamento = new HashMap<>();

        // Obter a lista de todas as compras. Esta parte depende de como seu repositório está configurado.
        // Suponha que você tem um método que retorna todas as compras:
        List<Compra> todasCompras = compraRepository.findAll();

        for (Compra compra : todasCompras) {
            // Extrair o mês da data da compra (assumindo que 'data' é uma String no formato "dd/MM/yyyy")
            String mes = compra.getData().substring(3, 5); // Extrai a parte do mês da data

            // Verificar se o mapa já contém uma entrada para este mês
            Map<String, Integer> detalhamentoMes = detalhamento.computeIfAbsent(mes, k -> new HashMap<>());

            // Percorrer os itens desta compra e agregar as quantidades
            for (ItemProduto item : compra.getItens()) {
                // Assumindo que 'item' tem um método 'getQuantidade()' e 'getProduto().getNome()'
                String nomeProduto = item.getProduto().getNome();
                Integer quantidade = item.getQuantidade();

                // Adicionar ou atualizar a contagem no mapa de detalhamento do mês
                detalhamentoMes.merge(nomeProduto, quantidade, Integer::sum);
            }
        }

        return detalhamento;
    }

    public Map<String, Map<String, Integer>> getVendasPorProdutoEMes() {
        // Este mapa vai armazenar o resultado final com a estrutura mês -> (produto -> quantidade)
        Map<String, Map<String, Integer>> vendasPorProdutoEMes = new HashMap<>();
    
        // Obter a lista de todas as compras. Esta parte depende de como seu repositório está configurado.
        // Suponha que você tem um método que retorna todas as compras:
        List<Compra> todasCompras = compraRepository.findAll();
    
        for (Compra compra : todasCompras) {
            // Extrair o mês da data da compra (assumindo que 'data' é uma String no formato "dd/MM/yyyy")
            String mes = compra.getData().substring(3, 5); // Extrai a parte do mês da data
    
            // Verificar se o mapa já contém uma entrada para este mês
            Map<String, Integer> vendasNoMes = vendasPorProdutoEMes.computeIfAbsent(mes, k -> new HashMap<>());
    
            // Percorrer os itens desta compra e agregar as quantidades vendidas
            for (ItemProduto item : compra.getItens()) {
                // Assumindo que 'item' tem um método 'getQuantidade()' e 'getProduto().getNome()'
                String nomeProduto = item.getProduto().getNome();
                Integer quantidade = item.getQuantidade();
    
                // Adicionar ou atualizar a contagem no mapa de vendas do mês
                vendasNoMes.merge(nomeProduto, quantidade, Integer::sum);
            }
        }
    
        return vendasPorProdutoEMes;
    }    

}
