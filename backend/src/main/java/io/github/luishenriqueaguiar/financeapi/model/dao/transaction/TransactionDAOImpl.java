package io.github.luishenriqueaguiar.financeapi.model.dao.transaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import io.github.luishenriqueaguiar.financeapi.dtos.FinancialSummaryDTO;
import io.github.luishenriqueaguiar.financeapi.model.entity.Transaction;

public class TransactionDAOImpl implements TransactionDAO {
	
	private DataSource dataSource;

    public TransactionDAOImpl() {
        try {
            InitialContext ctx = new InitialContext();
            this.dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/MyFinanceDB");
        } catch (Exception e) {
            throw new RuntimeException("Error initializing DAO: JNDI lookup failed", e);
        }
    }

    @Override
    public void create(Transaction transaction) throws Exception {
        String sql = "INSERT INTO transactions (description, value, type, category, transaction_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaction.getDescription());
            stmt.setBigDecimal(2, transaction.getValue());
            stmt.setString(3, transaction.getType());
            stmt.setString(4, transaction.getCategory());
            stmt.setDate(5, Date.valueOf(transaction.getTransactionDate()));

            stmt.executeUpdate();
        }
    }
    
    @Override
    public List<Transaction> findAll(String type, String category, Integer year, Integer month, Integer limit, Integer offset) throws Exception {
        List<Transaction> transactions = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM transactions");
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (type != null && !type.trim().isEmpty()) {
            conditions.add("type = ?");
            params.add(type);
        }

        if (category != null && !category.trim().isEmpty()) {
            conditions.add("category = ?");
            params.add(category);
        }

        if (year != null) {
            conditions.add("YEAR(transaction_date) = ?");
            params.add(year);
        }

        if (month != null) {
            conditions.add("MONTH(transaction_date) = ?");
            params.add(month);
        }

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }
        
        sql.append(" ORDER BY transaction_date DESC");
        
        if (limit != null) {
            sql.append(" LIMIT ?");
            params.add(limit);
        }

        if (offset != null) {
            sql.append(" OFFSET ?");
            params.add(offset);
        }
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setId(rs.getInt("id"));
                    transaction.setDescription(rs.getString("description"));
                    transaction.setValue(rs.getBigDecimal("value"));
                    transaction.setType(rs.getString("type"));
                    transaction.setCategory(rs.getString("category"));
                    transaction.setTransactionDate(rs.getDate("transaction_date").toLocalDate());

                    transactions.add(transaction);
                }
            }
        }
        return transactions;
    }
    
    @Override
    public Transaction findById(int id) throws Exception {
        String sql = "SELECT * FROM transactions WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setId(rs.getInt("id"));
                    transaction.setDescription(rs.getString("description"));
                    transaction.setValue(rs.getBigDecimal("value"));
                    transaction.setType(rs.getString("type"));
                    transaction.setCategory(rs.getString("category"));
                    transaction.setTransactionDate(rs.getDate("transaction_date").toLocalDate());
                    return transaction;
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean deleteById(int id) throws Exception {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        }
    }
    
    @Override
    public boolean update(Transaction transaction) throws Exception {
        String sql = "UPDATE transactions SET description = ?, value = ?, type = ?, category = ?, transaction_date = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaction.getDescription());
            stmt.setBigDecimal(2, transaction.getValue());
            stmt.setString(3, transaction.getType());
            stmt.setString(4, transaction.getCategory());
            stmt.setDate(5, Date.valueOf(transaction.getTransactionDate()));
            stmt.setInt(6, transaction.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    @Override
    public FinancialSummaryDTO getFinancialSummary() throws Exception {
        FinancialSummaryDTO summary = new FinancialSummaryDTO();
        String revenueSql = "SELECT SUM(value) as total FROM transactions WHERE type = 'REVENUE'";
        String expenseSql = "SELECT SUM(value) as total FROM transactions WHERE type = 'EXPENSE'";
        String expenseByCategorySql = "SELECT category, SUM(value) as total FROM transactions WHERE type = 'EXPENSE' GROUP BY category";

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(revenueSql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    summary.setTotalRevenue(rs.getBigDecimal("total"));
                }
            }

            if (summary.getTotalRevenue() == null) {
                summary.setTotalRevenue(BigDecimal.ZERO);
            }

            try (PreparedStatement stmt = conn.prepareStatement(expenseSql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    summary.setTotalExpense(rs.getBigDecimal("total"));
                }
            }

            if (summary.getTotalExpense() == null) {
                summary.setTotalExpense(BigDecimal.ZERO);
            }

            summary.setCurrentBalance(summary.getTotalRevenue().subtract(summary.getTotalExpense()));


            Map<String, BigDecimal> expensesByCategory = new HashMap<>();
            try (PreparedStatement stmt = conn.prepareStatement(expenseByCategorySql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    expensesByCategory.put(rs.getString("category"), rs.getBigDecimal("total"));
                }
            }
            summary.setExpensesByCategory(expensesByCategory);
        }
        
        return summary;
    }
}
