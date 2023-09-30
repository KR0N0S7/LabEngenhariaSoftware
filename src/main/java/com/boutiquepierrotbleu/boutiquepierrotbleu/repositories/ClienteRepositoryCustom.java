package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.List;

import com.boutiquepierrotbleu.boutiquepierrotbleu.dto.ClienteSearchCriteria;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;

public interface ClienteRepositoryCustom {
    List<Cliente> findByCriteria(ClienteSearchCriteria criteria);
}
