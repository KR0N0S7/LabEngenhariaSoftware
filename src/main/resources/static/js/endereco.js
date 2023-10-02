let enderecoIndex = 0; // Inicializa o índice com 0

function addEnderecoFields() {
// Get the enderecos container
    const container = document.getElementById('enderecos');

    const currentIndex = enderecoIndex++;

    const newEnderecoDiv = document.createElement('div');
    newEnderecoDiv.className = 'endereco';
    newEnderecoDiv.innerHTML = `
    <!-- Adicione um contador dentro do formulário de endereço -->
    <span class="endereco-count form">Seu formulário é o: ${currentIndex}</span>
    <div class="flex flex-col my-4">
        <input type="text" class="input-cep my-10" name="enderecos[${currentIndex}].cep" placeholder="CEP" required>
        <input type="text" class="input-rua my-10" name="enderecos[${currentIndex}].rua" placeholder="Rua" required>
        <input type="text" class="input-numero my-10"  name="enderecos[${currentIndex}].numero" placeholder="Número" required>
        <input type="text" class="input-bairro my-10" name="enderecos[${currentIndex}].bairro" placeholder="Bairro" required>
        <input type="text" class="input-cidade my-10" name="enderecos[${currentIndex}].cidade" placeholder="Cidade" required>
        <input type="text" class="input-estado my-10" name="enderecos[${currentIndex}].estado" placeholder="Estado" required>
    </div>
    `;

    container.appendChild(newEnderecoDiv);
}