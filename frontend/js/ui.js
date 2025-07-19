const moneyFormatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

function renderSummary(summary) {
  if (!summary) return;
  document.getElementById("summary-balance").textContent =
    moneyFormatter.format(summary.currentBalance || 0);
  document.getElementById("summary-revenue").textContent =
    moneyFormatter.format(summary.totalRevenue || 0);
  document.getElementById("summary-expense").textContent =
    moneyFormatter.format(summary.totalExpense || 0);

  renderExpensesChart(summary.expensesByCategory);
}

function renderTransactions(transactions) {
  const tbody = document.getElementById("transactions-tbody");
  tbody.innerHTML = "";

  if (transactions.length === 0) {
    tbody.innerHTML =
      '<tr><td colspan="5" class="text-center text-gray-500 py-4">Nenhuma transação encontrada.</td></tr>';
    return;
  }

  transactions.forEach((t) => {
    const tr = document.createElement("tr");
    tr.className = `border-b ${t.type === "REVENUE" ? "revenue" : "expense"}`;
    tr.innerHTML = `
            <td class="py-2 px-4">${t.description}</td>
            <td class="py-2 px-4 font-medium ${
              t.type === "REVENUE" ? "text-green-600" : "text-red-600"
            }">${moneyFormatter.format(t.value)}</td>
            <td class="py-2 px-4">${t.category}</td>
            <td class="py-2 px-4">${new Date(
              t.transactionDate
            ).toLocaleDateString()}</td>
            <td class="py-2 px-4 text-right">
                <button class="text-blue-500 hover:text-blue-700 mr-2"><i class="fas fa-edit"></i></button>
                <button class="text-red-500 hover:text-red-700"><i class="fas fa-trash"></i></button>
            </td>
        `;
    tbody.appendChild(tr);
  });
}

let expensesChart = null;
function renderExpensesChart(expensesData) {
  const ctx = document.getElementById("expenses-chart").getContext("2d");

  if (expensesChart) {
    expensesChart.destroy();
  }

  const labels = Object.keys(expensesData);
  const data = Object.values(expensesData);

  expensesChart = new Chart(ctx, {
    type: "doughnut",
    data: {
      labels: labels,
      datasets: [
        {
          label: "Despesas por Categoria",
          data: data,
          backgroundColor: [
            "#EF4444",
            "#F97316",
            "#EAB308",
            "#84CC16",
            "#22C55E",
            "#14B8A6",
            "#06B6D4",
            "#3B82F6",
            "#8B5CF6",
            "#D946EF",
          ],
          hoverOffset: 4,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: "top",
        },
      },
    },
  });
}
