const moneyFormatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

const modal = document.getElementById("transaction-modal");
const form = document.getElementById("transaction-form");

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

function updatePaginationControls(page, transactions) {
  const pageInfo = document.getElementById("page-info");
  const prevBtn = document.getElementById("prev-page-btn");
  const nextBtn = document.getElementById("next-page-btn");

  pageInfo.textContent = `Página ${page}`;
  prevBtn.disabled = page === 1;

  const limit = 10;
  nextBtn.disabled = transactions.length < limit;
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

function showTransactionModal(transaction = null) {
  form.reset();

  if (transaction) {
    document.getElementById("modal-title").textContent = "Editar Transação";
    document.getElementById("transaction-id").value = transaction.id;
    document.getElementById("description").value = transaction.description;
    document.getElementById("value").value = transaction.value;
    document.getElementById("type").value = transaction.type;
    document.getElementById("category").value = transaction.category;
    document.getElementById("transactionDate").value =
      transaction.transactionDate;
  } else {
    document.getElementById("modal-title").textContent = "Nova Transação";
    document.getElementById("transaction-id").value = "";
  }

  modal.classList.remove("hidden");
}

function closeTransactionModal() {
  modal.classList.add("hidden");
}

function showNotification(message, icon = "success") {
  Swal.fire({
    toast: true,
    position: "top-end",
    icon: icon,
    text: message,
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
  });
}

async function showConfirmationDialog(title, text) {
  const result = await Swal.fire({
    title: title,
    text: text,
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#8B5CF6",
    cancelButtonColor: "#d33",
    confirmButtonText: "Sim, excluir!",
    cancelButtonText: "Cancelar",
  });
  return result.isConfirmed;
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
                <button class="edit-btn text-blue-500 hover:text-blue-700 mr-2" data-id="${
                  t.id
                }"><i class="fas fa-edit"></i></button>
                <button class="delete-btn text-red-500 hover:text-red-700" data-id="${
                  t.id
                }"><i class="fas fa-trash"></i></button>
            </td>
        `;
    tbody.appendChild(tr);
  });
}
