package com.tpjad.warehouse.store;

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
    void approvesAndDecrementsWhenStockIsAvailable() {
        assertTrue(store.approveRequest("rose-red", 4));
        assertEquals(6, store.findAll().getFirst().getQuantity());
    }

    @Test
    void rejectsWithoutChangingStockWhenQuantityIsTooLarge() {
        assertFalse(store.approveRequest("rose-red", 11));
        assertEquals(10, store.findAll().getFirst().getQuantity());
    }

    @Test
    void rejectsUnknownFlowerAndNonPositiveQuantity() {
        assertFalse(store.approveRequest("unknown", 1));
        assertFalse(store.approveRequest("rose-red", 0));
        assertEquals(10, store.findAll().getFirst().getQuantity());
    }
}
