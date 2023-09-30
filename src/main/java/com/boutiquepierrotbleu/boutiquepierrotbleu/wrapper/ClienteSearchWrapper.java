package com.boutiquepierrotbleu.boutiquepierrotbleu.wrapper;

import java.util.List;

import com.boutiquepierrotbleu.boutiquepierrotbleu.dto.ClienteSearchCriteria;

public class ClienteSearchWrapper {
    private List<ClienteSearchCriteria> criteriaList;

    public List<ClienteSearchCriteria> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<ClienteSearchCriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    
}
