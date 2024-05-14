package com.boutiquepierrotbleu.boutiquepierrotbleu.dto;

public class PredictionResult {
    private Boolean isFraudulent;
    private Double score;
    
    public Boolean getIsFraudulent() {
        return isFraudulent;
    }
    public void setIsFraudulent(Boolean isFraudulent) {
        this.isFraudulent = isFraudulent;
    }
    public Double getScore() {
        return score;
    }
    public void setScore(Double score) {
        this.score = score;
    }

    
}
