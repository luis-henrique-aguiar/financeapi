package io.github.luishenriqueaguiar.financeapi.controller.command;

import java.io.BufferedReader;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.luishenriqueaguiar.financeapi.model.dao.transaction.TransactionDAO;
import io.github.luishenriqueaguiar.financeapi.model.dao.transaction.TransactionDAOImpl;
import io.github.luishenriqueaguiar.financeapi.model.entity.Transaction;
import io.github.luishenriqueaguiar.financeapi.utils.LocalDateAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UpdateTransactionCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pathInfo = request.getPathInfo();
        int id = Integer.parseInt(pathInfo.substring(1));

        BufferedReader reader = request.getReader();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        
        Transaction transaction = gson.fromJson(reader, Transaction.class);
        transaction.setId(id);

        TransactionDAO dao = new TransactionDAOImpl();
        boolean updated = dao.update(transaction);

        if (updated) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
        	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
