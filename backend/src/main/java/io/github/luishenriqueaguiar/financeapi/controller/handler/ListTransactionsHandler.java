package io.github.luishenriqueaguiar.financeapi.controller.handler;

import io.github.luishenriqueaguiar.financeapi.controller.command.ListTransactionsCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListTransactionsHandler extends AbstractHandler {

    @Override
    protected boolean canHandle(HttpServletRequest request) {
        String path = request.getPathInfo() == null ? "/" : request.getPathInfo();
        return request.getMethod().equals("GET") && path.equals("/transactions");
    }

    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        new ListTransactionsCommand().execute(request, response);
    }
}
