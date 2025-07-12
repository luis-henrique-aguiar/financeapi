package io.github.luishenriqueaguiar.financeapi.model.dao.transaction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

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
    public List<Transaction> findAll() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
        return transactions;
    }
}
