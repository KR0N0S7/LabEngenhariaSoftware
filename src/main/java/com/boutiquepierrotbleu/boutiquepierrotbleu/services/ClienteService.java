package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.dto.ClienteSearchCriteria;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.ClienteRepository;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CupomService cupomService;

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente obterCliente(Long id) throws Exception {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isEmpty()) {
            throw new Exception("Cliente não encontrado!");
        }
        return cliente.get();
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public void excluirCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<Cliente> searchClients(ClienteSearchCriteria criteria) {
        logger.debug("Received search criteria for service: {}", criteria);

        List<Cliente> foundClients = clienteRepository.findByCriteria(criteria);

        logger.debug("Found {} clients based on the given criteria.", foundClients.size());

        return foundClients;
    }

    public List<Cliente> listarClientesAtivos() {
        List<Cliente> allClients = clienteRepository.findAll();
        return allClients.stream().filter(cliente -> cliente.isAtivo()).collect(Collectors.toList());
    }

    public void toggleAtivoStatusById(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cliente Id:" + clienteId));
        cliente.setAtivo(!cliente.isAtivo()); // Toggle the ativo status
        clienteRepository.save(cliente);
    }

    public Cliente autenticarCliente(String email, String senha) throws Exception {
        Optional<Cliente> cliente = clienteRepository.findByEmailAndSenha(email, senha);
        if (cliente.isEmpty()) {
            throw new Exception("Cliente não encontrado!");
        }
        return cliente.get();
    }

    @Scheduled(cron = "0 4 19 * * *") // Executa à 0 sec 0 min 0 h (* * *) todos os dias
    public void verificarAniversariosEmitirCupons() {
        LocalDate hoje = LocalDate.now();

        System.out.println(hoje);
        System.out.println("Executando a tarefa única às 2:17 AM.");

        List<Cliente> clientesAniversariantes = clienteRepository.findByDataAniversario(hoje.getMonthValue(),
                hoje.getDayOfMonth());

        System.out.println(clientesAniversariantes.size() + " clientes aniversariantes encontrados.");

        for (Cliente cliente : clientesAniversariantes) {
            cupomService.criarCupomParaClienteAniversariante(cliente, 30.00);
        }
    }

}
