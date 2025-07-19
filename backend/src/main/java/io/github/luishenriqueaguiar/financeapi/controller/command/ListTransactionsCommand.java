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
        String type = request.getParameter("type");
        String category = request.getParameter("category");

        String yearStr = request.getParameter("year");
        String monthStr = request.getParameter("month");

        String limitStr = request.getParameter("limit");
        String pageStr = request.getParameter("page");

        int limit = (limitStr != null && !limitStr.trim().isEmpty()) ? Integer.parseInt(limitStr) : 10;
        int page = (pageStr != null && !pageStr.trim().isEmpty()) ? Integer.parseInt(pageStr) : 1;
        int offset = (page - 1) * limit;
        
        Integer year = null;
        if (yearStr != null && !yearStr.trim().isEmpty()) {
            year = Integer.parseInt(yearStr);
        }

        Integer month = null;
        if (monthStr != null && !monthStr.trim().isEmpty()) {
            month = Integer.parseInt(monthStr);
        }

        TransactionDAO dao = new TransactionDAOImpl();
        List<Transaction> transactions = dao.findAll(type, category, year, month, limit, offset);

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
