package com.boutiquepierrotbleu.boutiquepierrotbleu.integrations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.fasterxml.jackson.core.type.TypeReference;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Mono<List<String>> parseJsonToProductNames(String json) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode recommendationsNode = rootNode.path("recommendations");

            if (recommendationsNode.isArray()) {
                return Mono.just(objectMapper.readValue(recommendationsNode.toString(),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, String.class)));
            } else {
                return Mono.error(new RuntimeException("Invalid JSON: 'recommendations' is not an array"));
            }
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Error parsing JSON", e));
        }
    }

    public static List<String> parseJsonToNomeProdutoLista(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RecommendationWrapper wrapper = objectMapper.readValue(json, RecommendationWrapper.class);
        return wrapper.getRecommendations();
    }
}
