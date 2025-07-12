package io.github.luishenriqueaguiar.financeapi.controller.command;

import io.github.luishenriqueaguiar.financeapi.model.dao.transaction.TransactionDAO;
import io.github.luishenriqueaguiar.financeapi.model.dao.transaction.TransactionDAOImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteTransactionCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pathInfo = request.getPathInfo();
        String idStr = pathInfo.substring(1);
        int id = Integer.parseInt(idStr);

        TransactionDAO dao = new TransactionDAOImpl();
        boolean deleted = dao.deleteById(id);

        if (deleted) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Transaction not found to delete");
        }
    }
}
