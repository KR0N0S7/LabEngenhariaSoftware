package com.boutiquepierrotbleu.boutiquepierrotbleu.integrations;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.boutiquepierrotbleu.boutiquepierrotbleu.dto.NotasProdutosDTO;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.NotasProdutos;

import reactor.core.publisher.Mono;

@Component
public class RecomendaProduto {
    private final WebClient webClient;

    public RecomendaProduto(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://endereco-da-sua-api-de-recomendacoes").build();
    }

    public Mono<String> sendNotasProduto(List<NotasProdutos> notasProdutos) {
        List<NotasProdutosDTO> dtos = notasProdutos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return this.webClient.post()
                .uri("/recomendacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dtos)
                .retrieve()
                .bodyToMono(String.class);
    }

    private NotasProdutosDTO convertToDto(NotasProdutos notaProduto) {
        NotasProdutosDTO dto = new NotasProdutosDTO();
        dto.setNomeProduto(notaProduto.getProduto().getNome());
        dto.setNota(notaProduto.getNota().doubleValue());
        return dto;
    }

}
