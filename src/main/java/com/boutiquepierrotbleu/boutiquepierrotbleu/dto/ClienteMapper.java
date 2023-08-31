package com.boutiquepierrotbleu.boutiquepierrotbleu.dto;

import java.util.List;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Creditcard;

public class ClienteMapper {
    public static Cliente fromClientSearchCriteria(ClienteSearchCriteria clientSearchCriteria) {
        Cliente cliente = new Cliente();
        cliente.setId(clientSearchCriteria.getId());
        cliente.setNomeCompleto(clientSearchCriteria.getNomeCompleto());
        cliente.setEmail(clientSearchCriteria.getEmail());
        cliente.setCpf(clientSearchCriteria.getCpf());
        cliente.setTelefone(clientSearchCriteria.getTelefone());
        return cliente;
    }

    public static ClienteSearchCriteria fromCliente(Cliente cliente) {
        ClienteSearchCriteria cliSearch = new ClienteSearchCriteria();
        cliSearch.setId(cliente.getId());
        cliSearch.setNomeCompleto(cliente.getNomeCompleto()); 
        cliSearch.setEmail(cliente.getEmail()); 
        cliSearch.setCpf(cliente.getCpf());
        cliSearch.setTelefone(cliente.getTelefone()); 
        // cliSearch.setEndereco(cliente.getEndereco());
        // cliSearch.setCarrinhoCompra(cliente.getCarrinhoCompra());
        // List<Creditcard> cartaos.setCreditCard(cliente.getCreditcard());
        // cliSearch.cartaos[0].getNumeroCartao(); 
        // cliSearch.setCompra(cliente.getCompra()); 
        // cliSearch.setCupom(cliente.getCupom()); 
        // cliSearch.setAtivo(cliente.isAtivo());
        return cliSearch;
    }
}
