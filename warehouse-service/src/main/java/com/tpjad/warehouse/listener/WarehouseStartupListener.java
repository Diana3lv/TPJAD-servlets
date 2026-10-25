package com.tpjad.warehouse.listener;

import com.tpjad.warehouse.store.StockStore;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.IOException;
import java.io.InputStream;

@WebListener
public class WarehouseStartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        try (InputStream input = event.getServletContext().getResourceAsStream("/WEB-INF/classes/warehouse-stock.csv")) {
            StockStore.getInstance().loadFromCsv(input);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load warehouse stock", exception);
        }
    }
}
