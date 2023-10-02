
    // Seleciona todos os campos de entrada com a classe "phone-input"
    const phoneInputs = document.querySelectorAll('.phone-input');

    // Adiciona um ouvinte de evento de entrada para cada campo de entrada
    phoneInputs.forEach(input => {
        input.addEventListener('input', function () {
            // Remove todos os caracteres não numéricos
            let phoneNumber = this.value.replace(/\D/g, '');

            // Adiciona a máscara de telefone
            phoneNumber = phoneNumber.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');

            // Define o valor do campo de entrada com a máscara de telefone
            this.value = phoneNumber;
        });
    });

