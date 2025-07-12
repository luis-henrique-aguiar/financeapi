package io.github.luishenriqueaguiar.financeapi.controller.command;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.luishenriqueaguiar.financeapi.model.dao.transaction.TransactionDAO;
import io.github.luishenriqueaguiar.financeapi.model.dao.transaction.TransactionDAOImpl;
import io.github.luishenriqueaguiar.financeapi.model.entity.Transaction;
import io.github.luishenriqueaguiar.financeapi.utils.LocalDateAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListTransactionsCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TransactionDAO dao = new TransactionDAOImpl();
        List<Transaction> transactions = dao.findAll();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        String jsonResponse = gson.toJson(transactions);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}
