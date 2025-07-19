package io.github.luishenriqueaguiar.financeapi.controller.handler;

import io.github.luishenriqueaguiar.financeapi.controller.command.UpdateTransactionCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

public class UpdateTransactionHandler extends AbstractHandler {

    @Override
    protected boolean canHandle(HttpServletRequest request) {
        String path = request.getPathInfo();
        return request.getMethod().equals("PUT") && path != null && path.matches("^/transactions/\\d+$");
    }

    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = request.getPathInfo().replace("/transactions/", "/");

        HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
            @Override
            public String getPathInfo() {
                return path;
            }
        };

        new UpdateTransactionCommand().execute(wrappedRequest, response);
    }
}
