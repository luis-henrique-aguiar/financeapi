package io.github.luishenriqueaguiar.financeapi.controller;

import java.io.IOException;

import io.github.luishenriqueaguiar.financeapi.controller.factory.HandlerFactory;
import io.github.luishenriqueaguiar.financeapi.controller.handler.Handler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/*")
public class FrontController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Handler chain;

    @Override
    public void init() throws ServletException {
        try {
            this.chain = HandlerFactory.createChain();
        } catch (Exception e) {
            throw new ServletException("Error initializing handler chain", e);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (chain != null) {
                chain.handle(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "API endpoint not configured");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An internal error occurred.");
        }
    }
}
