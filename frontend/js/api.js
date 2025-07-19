const API_BASE_URL = "http://localhost:8080/financeapi/api";

async function fetchTransactions(filters = {}) {
  try {
    const params = new URLSearchParams();

    if (filters.type) params.append("type", filters.type);
    if (filters.category) params.append("category", filters.category);
    if (filters.year) params.append("year", filters.year);
    if (filters.month) params.append("month", filters.month);
    if (filters.page) params.append("page", filters.page);
    if (filters.limit) params.append("limit", filters.limit);

    const queryString = params.toString();
    const url = `${API_BASE_URL}/transactions${
      queryString ? "?" + queryString : ""
    }`;

    const response = await fetch(url);
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error("Falha ao buscar transações:", error);
    return [];
  }
}

async function fetchSummary() {
  try {
    const response = await fetch(`${API_BASE_URL}/transactions/summary`);
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error("Falha ao buscar resumo:", error);
    return null;
  }
}

async function addTransaction(transactionData) {
  try {
    const response = await fetch(`${API_BASE_URL}/transactions`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(transactionData),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return true;
  } catch (error) {
    console.error("Falha ao adicionar transação:", error);
    return false;
  }
}

async function deleteTransaction(id) {
  try {
    const response = await fetch(`${API_BASE_URL}/transactions/${id}`, {
      method: "DELETE",
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return true;
  } catch (error) {
    console.error(`Falha ao excluir transação ${id}:`, error);
    return false;
  }
}

async function fetchTransactionById(id) {
  try {
    const response = await fetch(`${API_BASE_URL}/transactions/${id}`);
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error(`Falha ao buscar transação ${id}:`, error);
    return null;
  }
}

async function updateTransaction(id, transactionData) {
  try {
    const response = await fetch(`${API_BASE_URL}/transactions/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(transactionData),
    });
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
    return true;
  } catch (error) {
    console.error(`Falha ao atualizar transação ${id}:`, error);
    return false;
  }
}
