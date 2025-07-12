package io.github.luishenriqueaguiar.financeapi.controller.command;

import java.io.PrintWriter;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.luishenriqueaguiar.financeapi.model.dao.transaction.TransactionDAO;
import io.github.luishenriqueaguiar.financeapi.model.dao.transaction.TransactionDAOImpl;
import io.github.luishenriqueaguiar.financeapi.model.entity.Transaction;
import io.github.luishenriqueaguiar.financeapi.utils.LocalDateAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FindTransactionByIdCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pathInfo = request.getPathInfo(); 
        String idStr = pathInfo.substring(1); 
        int id = Integer.parseInt(idStr);

        TransactionDAO dao = new TransactionDAOImpl();
        Transaction transaction = dao.findById(id);

        if (transaction != null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .setPrettyPrinting()
                    .create();
            
            String jsonResponse = gson.toJson(transaction);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Transaction not found");
        }
    }
}
