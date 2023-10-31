package com.boutiquepierrotbleu.boutiquepierrotbleu.entities;

public enum Status {
    PENDENTE, APROVADO, REPROVADO, ENTREGUE, CANCELADO, AGUARDANDO_PAGAMENTO;
    // em processamento - admin pode confirmar envio do pedido ou recusar o pagamento
    // o usuario solicita a troca
    // o admin aprova ou não a troca
    // quando o admin aceita a troca, pode confirmar o recebimento do produto, e assim o cupom é gerado com o mesmo valor do produto
}
