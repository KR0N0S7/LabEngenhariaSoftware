  // Função para formatar o CPF
  function formatarCPF(cpf) {
    // Remove todos os caracteres não numéricos
    const cleaned = ('' + cpf).replace(/\D/g, '');
    // Limita a quantidade de dígitos para 11
    const cpfLimitado = cleaned.substring(0, 11);
    // Aplica a máscara de CPF
    const match = cpfLimitado.match(/^(\d{3})(\d{3})(\d{3})(\d{2})$/);
    if (match) {
        return match[1] + '.' + match[2] + '.' + match[3] + '-' + match[4];
    }
    return cpfLimitado;
}

// Seleciona o input de CPF
const cpfInput = document.getElementById('cpf');
// Adiciona um ouvinte de evento de input
cpfInput.addEventListener('input', function(event) {
    // Obtém o valor atual do input
    const valor = event.target.value;
    // Formata o valor do CPF
    const cpfFormatado = formatarCPF(valor);
    // Define o valor formatado no input
    event.target.value = cpfFormatado;

    // Exibe ou oculta a mensagem de erro com base na validade do CPF
    const cpfError = document.getElementById('cpf-error');
    if (cpfFormatado.length !== 14) {
        cpfError.style.display = 'block';
    } else {
        cpfError.style.display = 'none';
    }
});