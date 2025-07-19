const API_BASE_URL = "http://localhost:8080/financeapi/api";

async function fetchTransactions() {
  try {
    const response = await fetch(`${API_BASE_URL}/transactions`);
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
