package io.github.luishenriqueaguiar.financeapi.model.dao.transaction;

import java.util.List;

import io.github.luishenriqueaguiar.financeapi.model.entity.Transaction;

public interface TransactionDAO {

    void create(Transaction transaction) throws Exception;
    
    List<Transaction> findAll() throws Exception;
    
    Transaction findById(int id) throws Exception;
    
    boolean deleteById(int id) throws Exception;
    
    boolean update(Transaction transaction) throws Exception;
}
