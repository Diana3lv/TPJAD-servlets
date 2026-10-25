package com.tpjad.shop.ui;

import com.tpjad.shop.client.WarehouseClient;
import com.tpjad.shop.model.StockRequestResult;
import com.tpjad.shop.store.StockStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("")
public class ShopServlet extends HttpServlet {
    private final StockStore stockStore = StockStore.getInstance();
    private final WarehouseClient warehouseClient = new WarehouseClient();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        render(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String flowerId = request.getParameter("flowerId");
        int quantity;
        try {
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException | NullPointerException exception) {
            render(request, response, "Quantity must be a positive integer.");
            return;
        }
        if (flowerId == null || flowerId.isBlank() || quantity <= 0) {
            render(request, response, "Flower and quantity are required.");
            return;
        }

        if ("decrease".equals(action)) {
            if (stockStore.decrease(flowerId, quantity)) {
                render(request, response, "Stock decreased.");
            } else {
                render(request, response, "Stock could not be decreased.");
            }
            return;
        }

        if ("request".equals(action)) {
            StockRequestResult result = warehouseClient.requestStock(flowerId, quantity);
            if (result.status() == StockRequestResult.Status.APPROVED) {
                if (!stockStore.increase(flowerId, quantity)) {
                    render(request, response, "Approved request could not update local stock.");
                } else {
                    render(request, response, result.message());
                }
            } else {
                render(request, response, result.message());
            }
            return;
        }

        render(request, response, "Unknown action.");
    }

    private void render(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("stock", stockStore.findAll());
        request.setAttribute("lowStockThreshold", StockStore.LOW_STOCK_THRESHOLD);
        request.setAttribute("message", message);
        request.getRequestDispatcher("/WEB-INF/jsp/stock.jsp").forward(request, response);
    }
}
