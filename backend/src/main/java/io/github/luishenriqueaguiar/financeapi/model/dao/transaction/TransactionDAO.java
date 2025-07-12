package io.github.luishenriqueaguiar.financeapi.model.dao.transaction;

import io.github.luishenriqueaguiar.financeapi.model.entity.Transaction;

public interface TransactionDAO {

    void create(Transaction transaction) throws Exception;
    
}
