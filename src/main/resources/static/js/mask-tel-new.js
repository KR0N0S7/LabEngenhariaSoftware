
function formatarTelefone(telefone) {
    // Remove todos os caracteres não numéricos
    const cleaned = ('' + telefone).replace(/\D/g, '');
    // Limita a quantidade de dígitos para 11 (incluindo o DDD)
    const telefoneLimitado = cleaned.substring(0, 11);
    // Aplica a máscara de telefone
    const match = telefoneLimitado.match(/^(\d{2})(\d{4,5})(\d{4})$/);
    if (match) {
        return '(' + match[1] + ') ' + match[2] + '-' + match[3];
    }
    return telefoneLimitado;
}

// Seleciona o input de telefone
const telefoneInput = document.getElementById('telefone');
// Adiciona um ouvinte de evento de input
telefoneInput.addEventListener('input', function (event) {
    // Obtém o valor atual do input
    const valor = event.target.value;
    // Formata o valor do telefone
    const telefoneFormatado = formatarTelefone(valor);
    // Define o valor formatado no input
    event.target.value = telefoneFormatado;

    // Exibe ou oculta a mensagem de erro com base na validade do telefone
    const telefoneError = document.getElementById('telefone-error');
    if (telefoneFormatado.length !== 14) {
        telefoneError.style.display = 'block';
    } else {
        telefoneError.style.display = 'none';
    }
});
