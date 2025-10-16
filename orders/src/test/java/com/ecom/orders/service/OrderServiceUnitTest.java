package com.ecom.orders.service;

import com.ecom.orders.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceUnitTest {

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 3 : génère une clé AES 256 bits et encode en Base64
    @Test
    void generateAndEncryptKeyForDB_shouldReturnNonEmptyString() throws Exception {
        String key = orderService.generateAndEncryptKeyForDB();
        assertNotNull(key);
        assertFalse(key.isEmpty());
    }
}
