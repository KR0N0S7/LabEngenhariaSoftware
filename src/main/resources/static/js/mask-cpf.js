
// Seleciona todos os campos de entrada com a classe "cpf-input"
    const cpfInputs = document.querySelectorAll('.cpf-input');

    // Adiciona um ouvinte de evento de entrada para cada campo de entrada
    cpfInputs.forEach(input => {
        input.addEventListener('input', function () {
            // Remove todos os caracteres não numéricos
            let cpf = this.value.replace(/\D/g, '');

            // Adiciona a máscara de CPF
            cpf = cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');

            // Define o valor do campo de entrada com a máscara de CPF
            this.value = cpf;
        });
    });

