package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Compra;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cupom;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Troca;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.CupomRepository;

@Service
public class CupomService {
    @Autowired
    private CupomRepository cupomRepository;

    public Cupom salvarCupom(Cupom cupom) {
        return cupomRepository.save(cupom);
    }

    public Cupom obterCupom(Long id) throws Exception {
        Optional<Cupom> cupom = cupomRepository.findById(id);
        if (cupom.isEmpty()) {
            throw new Exception("Cupom não existe!");
        }
        return cupom.get();
    }

    public List<Cupom> listarCupons() {
        return cupomRepository.findAll();
    }

    public void excluirCupom(Long id) {
        cupomRepository.deleteById(id);
    }

    public Double aplicarCupom(Cupom cupom, Double valorOriginal) {
        return cupom.aplicarDesconto(valorOriginal);
    }

    public Cupom gerarCupomDesconto(Cliente cliente, Compra compra, Double porcentagem) {
        String tipo = "Desconto";
        Cupom novoCupom = new Cupom();
        novoCupom.gerarCupom(cliente, compra, tipo, porcentagem);

        // Save the coupon in the database
        cupomRepository.save(novoCupom);

        return novoCupom;
    }

    public Cupom gerarCupomTroca(Cliente cliente, Compra compra, Troca troca, Double valor) {
        String tipo = "Troca";
        Cupom novoCupom = new Cupom();
        novoCupom.gerarCupom(cliente, compra, tipo, valor);

        return cupomRepository.save(novoCupom);
    }

    public boolean validateCupom(String cupomCodigo) {
        // Check if the cupom with the provided code exists
        Optional<Cupom> existingCupom = cupomRepository.findByCodigo(cupomCodigo);

        // If the cupom does not exist, return false
        if (existingCupom.isEmpty()) {
            return false;
        }

        Cupom cupom = existingCupom.get();

        // Additional validation logic here.
        // For example, check if the cupom is not expired or has not been used, etc.
        // Check if the coupon is expired
        if (cupom.getDataValidade().before(Date.valueOf(LocalDate.now()))) {
            return false;
        }

        // Check if the coupon has been used too many times
        if (cupom.getUsoContador() >= cupom.getUsoLimite()) {
            return false;
        }

        // If all checks pass, return true
        return true;
    }

    public List<Cupom> listarCuponsByClienteId(Long clienteId) {
        return cupomRepository.findAllByClienteId(clienteId);
    }

    public double getDiscountAmount(String couponCode) {
        return cupomRepository.findByCodigo(couponCode).get().getValor();
    }

    public Cupom criarCupomParaClienteAniversariante(Cliente cliente, Double porcentagem) {
        Cupom novoCupom = new Cupom();
        novoCupom.gerarCupomAniversario(cliente, porcentagem);

        // Save the coupon in the database
        cupomRepository.save(novoCupom);

        return novoCupom;
    }

    public Cupom obterCupomByCodigo(String codigoCupom) throws Exception {
        Optional<Cupom> cupom = cupomRepository.findByCodigo(codigoCupom);
        if (cupom.isEmpty()) {
            throw new Exception("Cupom não existe!");
        }
        return cupom.get();
    }

}
