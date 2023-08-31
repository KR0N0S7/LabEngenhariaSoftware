package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.criteriaFilter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.boutiquepierrotbleu.boutiquepierrotbleu.dto.ClienteSearchCriteria;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.ClienteRepositoryCustom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ClienteRepositoryImpl implements ClienteRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(ClienteRepositoryImpl.class);

    @Override
    public List<Cliente> findByCriteria(ClienteSearchCriteria criteria) {
        logger.info("Received search criteria: {}", criteria);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> clientRoot = criteriaQuery.from(Cliente.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(criteria.getNomeCompleto())) {
            predicates.add(criteriaBuilder.like(clientRoot.get("nomeCompleto"), "%" + criteria.getNomeCompleto() + "%"));
        }

        if (StringUtils.hasText(criteria.getEmail())) {
            predicates.add(criteriaBuilder.like(clientRoot.get("email"), "%" + criteria.getEmail() + "%"));
        }

        if (StringUtils.hasText(criteria.getCpf())) {
            predicates.add(criteriaBuilder.equal(clientRoot.get("cpf"), criteria.getCpf()));
        }

        if (StringUtils.hasText(criteria.getTelefone())) {
            predicates.add(criteriaBuilder.like(clientRoot.get("telefone"), "%" + criteria.getTelefone() + "%"));
        }

        // if (criteria.getAtivo() != null) {
        //     predicates.add(criteriaBuilder.equal(clientRoot.get("ativo"), criteria.getAtivo()));
        // }

        // Add more predicates as required for other fields...

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Cliente> query = entityManager.createQuery(criteriaQuery);

        logger.debug("Query: {}", query);

        List<Cliente> clients = query.getResultList();

        logger.info("Found {} clients based on criteria.", clients.size());
        return clients;
    }

}
