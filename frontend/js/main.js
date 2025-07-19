document.addEventListener("DOMContentLoaded", () => {
  async function refreshDashboard() {
    const [summary, transactions] = await Promise.all([
      fetchSummary(),
      fetchTransactions(),
    ]);

    renderSummary(summary);
    renderTransactions(transactions);
  }

  refreshDashboard();
});
