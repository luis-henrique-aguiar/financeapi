package io.github.luishenriqueaguiar.financeapi.controller.handler;

import io.github.luishenriqueaguiar.financeapi.controller.command.GetFinancialSummaryCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetFinancialSummaryHandler extends AbstractHandler {

    @Override
    protected boolean canHandle(HttpServletRequest request) {
        return request.getMethod().equals("GET") && 
               request.getPathInfo() != null && 
               request.getPathInfo().equals("/transactions/summary");
    }

    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        new GetFinancialSummaryCommand().execute(request, response);
    }
}
