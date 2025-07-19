document.addEventListener("DOMContentLoaded", () => {
  const addTransactionBtn = document.getElementById("add-transaction-btn");
  const transactionForm = document.getElementById("transaction-form");
  const cancelBtn = document.getElementById("cancel-btn");
  const transactionsTbody = document.getElementById("transactions-tbody");
  const filterBtn = document.getElementById("filter-btn");
  const clearFilterBtn = document.getElementById("clear-filter-btn");
  const filterType = document.getElementById("filter-type");
  const filterMonth = document.getElementById("filter-month");
  const filterYear = document.getElementById("filter-year");
  const prevPageBtn = document.getElementById("prev-page-btn");
  const nextPageBtn = document.getElementById("next-page-btn");

  let currentPage = 1;
  let currentFilters = {};

  async function refreshDashboard(filters = {}, page = 1) {
    currentPage = page;
    currentFilters = filters;

    const paginationParams = { page: currentPage, limit: 10 };
    const allParams = { ...filters, ...paginationParams };

    const [summary, transactions] = await Promise.all([
      fetchSummary(),
      fetchTransactions(allParams),
    ]);

    renderSummary(summary);
    renderTransactions(transactions);
    updatePaginationControls(currentPage, transactions);
  }

  addTransactionBtn.addEventListener("click", () => {
    showTransactionModal();
  });

  cancelBtn.addEventListener("click", () => {
    closeTransactionModal();
  });

  filterBtn.addEventListener("click", () => {
    const filters = {
      type: filterType.value,
      month: filterMonth.value,
      year: filterYear.value,
    };
    refreshDashboard(filters, 1);
  });

  clearFilterBtn.addEventListener("click", () => {
    filterType.value = "";
    filterMonth.value = "";
    filterYear.value = "";
    refreshDashboard({}, 1);
  });

  prevPageBtn.addEventListener("click", () => {
    if (currentPage > 1) {
      refreshDashboard(currentFilters, currentPage - 1);
    }
  });

  nextPageBtn.addEventListener("click", () => {
    refreshDashboard(currentFilters, currentPage + 1);
  });

  transactionForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const id = document.getElementById("transaction-id").value;
    const transactionData = {
      description: document.getElementById("description").value,
      value: parseFloat(document.getElementById("value").value),
      type: document.getElementById("type").value,
      category: document.getElementById("category").value,
      transactionDate: document.getElementById("transactionDate").value,
    };

    let success = false;
    if (id) {
      success = await updateTransaction(id, transactionData);
    } else {
      success = await addTransaction(transactionData);
    }

    if (success) {
      closeTransactionModal();
      await refreshDashboard();
      showNotification(`Transação ${id ? "atualizada" : "salva"} com sucesso!`);
    } else {
      showNotification("Erro ao salvar a transação.", "error");
    }
  });

  transactionsTbody.addEventListener("click", async (event) => {
    const editButton = event.target.closest(".edit-btn");
    const deleteButton = event.target.closest(".delete-btn");

    if (editButton) {
      const transactionId = editButton.dataset.id;
      const transaction = await fetchTransactionById(transactionId);
      if (transaction) {
        showTransactionModal(transaction);
      }
    }

    if (deleteButton) {
      const transactionId = deleteButton.dataset.id;
      const confirmed = await showConfirmationDialog(
        "Confirmar Exclusão",
        "Você tem certeza?"
      );
      if (confirmed) {
        const success = await deleteTransaction(transactionId);
        if (success) {
          showNotification("Transação excluída com sucesso!");
          await refreshDashboard();
        } else {
          showNotification("Falha ao excluir a transação.", "error");
        }
      }
    }
  });

  refreshDashboard();
});
