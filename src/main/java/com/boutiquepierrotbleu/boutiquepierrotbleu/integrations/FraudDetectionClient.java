package com.boutiquepierrotbleu.boutiquepierrotbleu.integrations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.boutiquepierrotbleu.boutiquepierrotbleu.dto.PredictionResult;
import com.boutiquepierrotbleu.boutiquepierrotbleu.dto.TransactionData;

@Component
public class FraudDetectionClient {

    /*
     * @Value("${http://127.0.0.1:5002/predict}")
     * private String fraudApiUrl;
     * 
     * @Autowired
     * private RestTemplate restTemplate;
     * 
     * public PredictionResult detectFraud(TransactionData transactionData) {
     * HttpHeaders headers = new HttpHeaders();
     * headers.setContentType(MediaType.APPLICATION_JSON);
     * 
     * HttpEntity<TransactionData> request = new HttpEntity<>(transactionData,
     * headers);
     * 
     * PredictionResult result = restTemplate.postForObject(fraudApiUrl +
     * "/predict", request, PredictionResult.class);
     * return result;
     * }
     */
}
