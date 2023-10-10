package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.CarrinhoCompra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;

public interface CarrinhoCompraRepository extends JpaRepository<CarrinhoCompra, Long> {

    Optional<CarrinhoCompra> findByCliente(Cliente cliente);

}
