package com.boutiquepierrotbleu.boutiquepierrotbleu.integrations;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.boutiquepierrotbleu.boutiquepierrotbleu.dto.NotasProdutosDTO;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.NotasProdutos;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
public class RecomendaProduto {
    private final WebClient webClient;

    public RecomendaProduto(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5000/").build();
    }

    public Mono<String> sendNotasProduto(List<NotasProdutos> globalNotasProdutos,
            List<NotasProdutos> usuarioNotasProdutos) {
        List<NotasProdutosDTO> globalDtos = globalNotasProdutos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        List<NotasProdutosDTO> userDtos = usuarioNotasProdutos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // Create an object that holds both lists
        RatingsContainer ratingsContainer = new RatingsContainer(globalDtos, userDtos);

        return this.webClient.post()
                .uri("/recommend/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ratingsContainer)
                .retrieve()
                .bodyToMono(String.class);

    }

    private NotasProdutosDTO convertToDto(NotasProdutos notaProduto) {
        NotasProdutosDTO dto = new NotasProdutosDTO();
        dto.setClienteId(notaProduto.getCliente().getId().toString());
        dto.setNomeProduto(notaProduto.getProduto().getNome());
        dto.setNota(notaProduto.getNota().doubleValue());
        return dto;
    }

    private static class RatingsContainer {
        private List<NotasProdutosDTO> globalRatings;
        private List<NotasProdutosDTO> userRatings;

        public RatingsContainer(List<NotasProdutosDTO> globalRatings, List<NotasProdutosDTO> userRatings) {
            this.globalRatings = globalRatings;
            this.userRatings = userRatings;
        }

        // Getters and Setters
        public List<NotasProdutosDTO> getGlobalRatings() {
            return globalRatings;
        }

        public void setGlobalRatings(List<NotasProdutosDTO> globalRatings) {
            this.globalRatings = globalRatings;
        }

        public List<NotasProdutosDTO> getUserRatings() {
            return userRatings;
        }

        public void setUserRatings(List<NotasProdutosDTO> userRatings) {
            this.userRatings = userRatings;
        }
    }
}
