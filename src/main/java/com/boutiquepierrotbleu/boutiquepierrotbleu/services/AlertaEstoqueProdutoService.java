package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.AlertaEstoqueProduto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.ProdutoRepository;

@Service
public class AlertaEstoqueProdutoService {
    private final ProdutoRepository produtoRepository;
    private final MailService mailService; // Example, could be SMS, etc.

    @Autowired
    public AlertaEstoqueProdutoService(ProdutoRepository produtoRepository, MailService mailService) {
        this.produtoRepository = produtoRepository;
        this.mailService = mailService;
    }

    @Scheduled(cron = "0 0 * * * ?") // Adjust cron for your desired check frequency
    public void checkAndSendAlerts() {
        List<Produto> products = produtoRepository.findByEstoqueGreaterThanAndAviseMeTrue(0);

        for (Produto produto : products) {
            for (AlertaEstoqueProduto alert : produto.getAlertaEstoqueProduto()) {
                mailService.sendMail(alert.getCliente().getEmail(),
                        "Product Back in Stock!",
                        "Your desired product is available: " + produto.getNome());
            }
            // Potentially clear 'aviseMe' after sending
        }
    }
}
