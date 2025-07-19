## Documentação da API MyFinance

Esta documentação detalha os endpoints disponíveis na MyFinanceAPI para gerenciamento de transações financeiras pessoais.

### 1\. Listar Transações

**Descrição:** Retorna uma lista de todas as transações cadastradas, com suporte a filtros e paginação.

  * **Método HTTP:** `GET`
  * **Endpoint:** `/financeapi/api/transactions`
  * **Parâmetros de Query (Opcionais):**
      * `type` (string): Filtra por tipo. Valores possíveis: `REVENUE`, `EXPENSE`.
      * `category` (string): Filtra por uma categoria específica (ex: `Alimentação`).
      * `year` (int): Filtra por ano (ex: `2025`).
      * `month` (int): Filtra por mês (ex: `7`).
      * `page` (int): O número da página a ser retornada. Padrão: `1`.
      * `limit` (int): O número de itens por página. Padrão: `10`.
  * **Corpo da Requisição:** Nenhum.
  * **Exemplo de Resposta (200 OK):**
    ```json
    [
        {
            "id": 1,
            "description": "Salário mensal",
            "value": 6500.00,
            "type": "REVENUE",
            "category": "Salário",
            "transactionDate": "2025-07-05"
        },
        {
            "id": 2,
            "description": "Aluguel do apartamento",
            "value": 1800.00,
            "type": "EXPENSE",
            "category": "Moradia",
            "transactionDate": "2025-07-10"
        }
    ]
    ```
  * **Códigos de Resposta:**
      * `200 OK`: Requisição bem-sucedida. Retorna a lista de transações.
      * `500 Internal Server Error`: Erro interno no servidor ao buscar os dados.

### 2\. Cadastrar Nova Transação

**Descrição:** Cadastra uma nova transação financeira no banco de dados.

  * **Método HTTP:** `POST`
  * **Endpoint:** `/financeapi/api/transactions`
  * **Corpo da Requisição (JSON):**
    ```json
    {
        "description": "Compra de livro",
        "value": 95.50,
        "type": "EXPENSE",
        "category": "Educação",
        "transactionDate": "2025-07-19"
    }
    ```
  * **Códigos de Resposta:**
      * `201 Created`: Transação criada com sucesso.
      * `400 Bad Request`: JSON inválido ou dados ausentes.
      * `500 Internal Server Error`: Erro ao persistir os dados no banco.

### 3\. Buscar Transação por ID

**Descrição:** Retorna uma transação cadastrada no banco de dados.

  * **Método HTTP:** `GET`
  * **Endpoint:** `/financeapi/api/transactions/{id}`
  * **Parâmetros de URL:**
      * `id` (int): O identificador único da transação.
  * **Exemplo de Resposta (200 OK):**
    ```json
    {
      "id": 4,
      "description": "Aluguel do apartamento",
      "value": 1800.00,
      "type": "EXPENSE",
      "category": "Moradia",
      "transactionDate": "2025-07-10"
    }
    ```
  * **Códigos de Resposta:**
      * `200 OK`: Transação encontrada e retornada.
      * `404 Not Found`: Nenhuma transação encontrada com o ID fornecido.
      * `500 Internal Server Error`: Erro interno no servidor.

### 4\. Atualizar Transação

**Descrição:** Atualiza os dados de uma transação cadastrada no banco de dados.

  * **Método HTTP:** `PUT`
  * **Endpoint:** `/financeapi/api/transactions/{id}`
  * **Parâmetros de URL:**
      * `id` (int): O ID da transação a ser atualizada.
  * **Corpo da Requisição (JSON):**
  ```json
    {
      "id": 4,
      "description": "Aluguel do apartamento",
      "value": 1800.00,
      "type": "EXPENSE",
      "category": "Moradia",
      "transactionDate": "2025-07-10"
    }
  ```
  * **Códigos de Resposta:**
      * `200 OK`: Transação atualizada com sucesso.
      * `400 Bad Request`: JSON inválido ou dados ausentes.
      * `404 Not Found`: Nenhuma transação encontrada com o ID para atualizar.
      * `500 Internal Server Error`: Erro interno no servidor.

### 5\. Excluir Transação

**Descrição:** Exclui uma transação cadastrada no banco de dados.

  * **Método HTTP:** `DELETE`
  * **Endpoint:** `/financeapi/api/transactions/{id}`
  * **Parâmetros de URL:**
      * `id` (int): O ID da transação a ser excluída.
  * **Corpo da Requisição:** Nenhum.
  * **Códigos de Resposta:**
      * `204 No Content`: Transação excluída com sucesso.
      * `404 Not Found`: Nenhuma transação encontrada com o ID para excluir.
      * `500 Internal Server Error`: Erro interno no servidor.

### 6\. Obter Resumo Financeiro

**Descrição:** Recebe um resumo financeiro das transações cadastradas, por tipo de transação e por categorias.

  * **Método HTTP:** `GET`
  * **Endpoint:** `/financeapi/api/transactions/summary`
  * **Exemplo de Resposta (200 OK):**
   ```json
    {
      "totalRevenue": 90680.30,
      "totalExpense": 34139.95,
      "currentBalance": 56540.35,
      "expensesByCategory": {
          "Contas": 4429.90,
          "Transporte": 2245.50,
          "Alimentação": 4617.00,
          "Moradia": 18550.90,
          "Educação": 726.00,
          "Vestuário": 1070.00,
          "Saúde": 450.75,
          "Lazer": 2049.90
      }
    }
   ```
  * **Códigos de Resposta:**
      * `200 OK`: Resumo gerado com sucesso.
      * `500 Internal Server Error`: Erro interno no servidor ao calcular o resumo.
