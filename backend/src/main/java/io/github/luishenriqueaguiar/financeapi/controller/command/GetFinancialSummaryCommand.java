package io.github.luishenriqueaguiar.financeapi.controller.command;

import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.luishenriqueaguiar.financeapi.dtos.FinancialSummaryDTO;
import io.github.luishenriqueaguiar.financeapi.model.dao.transaction.TransactionDAO;
import io.github.luishenriqueaguiar.financeapi.model.dao.transaction.TransactionDAOImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetFinancialSummaryCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TransactionDAO dao = new TransactionDAOImpl();
        FinancialSummaryDTO summary = dao.getFinancialSummary();
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResponse = gson.toJson(summary);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}
