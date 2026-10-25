package com.tpjad.warehouse.ui;

import com.tpjad.warehouse.store.StockStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("")
public class WarehouseUiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("stock", StockStore.getInstance().findAll());
        request.getRequestDispatcher("/WEB-INF/jsp/stock.jsp").forward(request, response);
    }
}
