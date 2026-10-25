package com.tpjad.shop.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockStoreTest {
    private final StockStore store = StockStore.getInstance();

    @BeforeEach
    void setUp() throws Exception {
        store.loadFromCsv(new ByteArrayInputStream(
                "rose-red,Red Rose,10\n".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void increasesAndDecreasesStock() {
        assertTrue(store.increase("rose-red", 5));
        assertTrue(store.decrease("rose-red", 3));
        assertEquals(12, store.findAll().getFirst().getQuantity());
    }

    @Test
    void rejectsInvalidDecreaseWithoutChangingStock() {
        assertFalse(store.decrease("rose-red", 11));
        assertFalse(store.decrease("unknown", 1));
        assertEquals(10, store.findAll().getFirst().getQuantity());
    }

    @Test
    void exposesTheConfiguredLowStockThreshold() {
        assertTrue(9 < StockStore.LOW_STOCK_THRESHOLD);
        assertFalse(10 < StockStore.LOW_STOCK_THRESHOLD);
    }
}
